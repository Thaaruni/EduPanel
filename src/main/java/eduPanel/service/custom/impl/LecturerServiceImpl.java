package eduPanel.service.custom.impl;


import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.exception.AppException;
import eduPanel.repository.RepositoryFactory;
import eduPanel.repository.custom.LecturerRepository;
import eduPanel.repository.custom.LinkedInRepository;
import eduPanel.repository.custom.PictureRepository;
import eduPanel.service.custom.LecturerService;
import eduPanel.service.util.Transformer;
import eduPanel.store.AppStore;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo ;
import eduPanel.util.LecturerType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LECTURER);
    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LINKEDIN);
    private final PictureRepository pictureRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.PICTURE);
    private final Transformer transformer = new Transformer();

    public LecturerServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LectureTo saveLecturer(LecturerReqTo LecturerReqTo) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer lecturer = transformer.fromLecturerReqTo(LecturerReqTo);
            lecturerRepository.save(lecturer);

            if (LecturerReqTo.getLinkedin() != null) {
                linkedInRepository.save(lecturer.getLinkedIn());
            }

            String pictureUrl = null;
            if (LecturerReqTo.getPicture() != null) {
                // Save Picture entity
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId() );
                pictureRepository.save(picture);

                // Ensure upload directory exists
                Path uploadsDir = Paths.get("uploads", "lecturers");
                if (!Files.exists(uploadsDir)) {
                    Files.createDirectories(uploadsDir);
                }

                // Save file to disk
                Path picturePath = uploadsDir.resolve(lecturer.getId() + ".png");
                Files.copy(LecturerReqTo.getPicture().getInputStream(), picturePath, StandardCopyOption.REPLACE_EXISTING);

                // Set local file path as URL (could be served from a controller or directly if using static handler)
                pictureUrl = "/home/thaaruni-dissanayake/Documents/DEP13/my_projects/edupanel-jave-api/src/main/java/eduPanel/uploads/lecturers" + lecturer.getId() + ".png";
            }

            AppStore.getEntityManager().getTransaction().commit();

            LectureTo lectureTo = transformer.toLecturerTo(lecturer);
            lectureTo.setPicture(pictureUrl);
            return lectureTo;
        } catch (Throwable t) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException(500, "Failed to save the lecturer", t);
        }
    }



    @Override
    public void updateLecturerDetails(LecturerReqTo lecturerReqTo) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerReqTo.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
        Lecturer currentLecturer = optLecturer.get();

        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer newLecturer = transformer.fromLecturerReqTo(lecturerReqTo);

            if (lecturerReqTo.getPicture() != null) {
                newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + currentLecturer.getId() + ".png"));
            }

            if (lecturerReqTo.getLinkedin() != null) {
                newLecturer.setLinkedIn(new LinkedIn(newLecturer, lecturerReqTo.getLinkedin()));
            }

            updateLinkedIn(currentLecturer, newLecturer);

            // Handle picture logic
            Path uploadsDir = Paths.get("uploads", "lecturers");
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }

            Path picturePath = uploadsDir.resolve(currentLecturer.getId() + ".png");

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                pictureRepository.save(newLecturer.getPicture());
                Files.copy(lecturerReqTo.getPicture().getInputStream(), picturePath, StandardCopyOption.REPLACE_EXISTING);
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                pictureRepository.deleteById(currentLecturer.getId());
                Files.deleteIfExists(picturePath);
            } else if (newLecturer.getPicture() != null) {
                pictureRepository.update(newLecturer.getPicture());
                Files.copy(lecturerReqTo.getPicture().getInputStream(), picturePath, StandardCopyOption.REPLACE_EXISTING);
            }

            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        } catch (Throwable t) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException(500, "Failed to update the lecturer details", t);
        }
    }

    @Override
    public void updateLecturerDetails(LectureTo lectureTo) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lectureTo.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
        Lecturer currentLecturer = optLecturer.get();
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer newLecturer = transformer.fromLecturerTo(lectureTo);
            newLecturer.setPicture(currentLecturer.getPicture());
            updateLinkedIn(currentLecturer, newLecturer);
            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }



    @Override
    public void deleteLecturer(Integer lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) throw new AppException(404, "No lecturer found");
        AppStore.getEntityManager().getTransaction().begin();
        try {
            lecturerRepository.deleteById(lecturerId);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public LectureTo getLecturerDetails(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerId);
            if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
            AppStore.getEntityManager().getTransaction().commit();

            Lecturer lecturer = optLecturer.get();
            LectureTo lectureTo = transformer.toLecturerTo(lecturer);

            if (lecturer.getPicture() != null) {
                // Set the picture URL to a static endpoint (e.g., served from /uploads/lecturers/)
                String pictureUrl = "/uploads/lecturers/" + lecturer.getId() + ".png";
                lectureTo.setPicture(pictureUrl);
            }

            return lectureTo;
        } catch (Throwable t) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public List<LectureTo> getLecturers(LecturerType type) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            List<Lecturer> lecturerList;
            if (type == LecturerType.FULL_TIME) {
                lecturerList = lecturerRepository.findFullTimeLecturers();
            } else if (type == LecturerType.VISITING) {
                lecturerList = lecturerRepository.findVisitingLectures();
            } else {
                lecturerList = lecturerRepository.findAll();
            }

            AppStore.getEntityManager().getTransaction().commit();

            return lecturerList.stream().map(l -> {
                LectureTo lectureTo = transformer.toLecturerTo(l);
                if (l.getPicture() != null) {
                    // Use local path for serving picture
                    String pictureUrl = "/uploads/lecturers/" + l.getId() + ".png";
                    lectureTo.setPicture(pictureUrl);
                }
                return lectureTo;
            }).collect(Collectors.toList());

        } catch (Throwable t) {
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }


    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer){
        if (newLecturer.getLinkedIn() != null && currentLecturer.getLinkedIn() == null) {
            linkedInRepository.save(newLecturer.getLinkedIn());
        } else if (newLecturer.getLinkedIn() == null && currentLecturer.getLinkedIn() != null) {
            linkedInRepository.deleteById(currentLecturer.getId());
        } else if (newLecturer.getLinkedIn() != null) {
            linkedInRepository.update(newLecturer.getLinkedIn());
        }
    }
}