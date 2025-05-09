package eduPanel.service.custom.impl;


import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.exception.AppException;
import eduPanel.repository.LecturerRepository;
import eduPanel.repository.LinkedInRepository;
import eduPanel.repository.PictureRepository;
import eduPanel.service.custom.LecturerService;
import eduPanel.service.util.Transformer;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import eduPanel.util.LecturerType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Transactional
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final LinkedInRepository linkedInRepository;
    private final PictureRepository pictureRepository;
    private final Transformer transformer;


    public LecturerServiceImpl(LecturerRepository lecturerRepository, LinkedInRepository linkedInRepository, PictureRepository pictureRepository, Transformer transformer) {
        this.lecturerRepository = lecturerRepository;
        this.linkedInRepository = linkedInRepository;
        this.pictureRepository = pictureRepository;
        this.transformer = transformer;

    }

    @Override
    public LectureTo saveLecturer(LecturerReqTo LecturerReqTo) {
        Lecturer lecturer = transformer.fromLecturerReqTo(LecturerReqTo);
        lecturerRepository.save(lecturer);

        if (LecturerReqTo.getLinkedin() != null) {
            linkedInRepository.save(lecturer.getLinkedIn());
        }

        String pictureUrl = null;
        if (LecturerReqTo.getPicture() != null && !LecturerReqTo.getPicture().isEmpty()) {
            String picturePath = "lecturers/" + lecturer.getId() + ".png";
            Path savePath = Paths.get("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-eduPanel/eduPanel-jave-api/src/main/java/eduPanel/uploads", picturePath); // Change this to your actual upload location

            try {
                Files.createDirectories(savePath.getParent());
                LecturerReqTo.getPicture().transferTo(savePath.toFile());
            } catch (IOException e) {
                throw new AppException(500, "Failed to upload the image", e);
            }

            Picture picture = new Picture(lecturer, picturePath);
            pictureRepository.save(picture);
            pictureUrl = "/uploads/" + picturePath; // This assumes Spring is serving static files from /uploads
        }

        LectureTo LectureTo = transformer.toLectureTo(lecturer);
        LectureTo.setPicture(pictureUrl);
        return LectureTo;
    }

    @Override
    public void updateLecturerDetails(LecturerReqTo LecturerReqTo) {
        Lecturer currentLecturer = lecturerRepository.findById(LecturerReqTo.getId())
                .orElseThrow(() -> new AppException(404, "No lecturer associated with the ID"));

        // Remove old picture from filesystem and database
        if (currentLecturer.getPicture() != null) {
            String oldPicturePath = currentLecturer.getPicture().getPicturePath();
            File oldFile = new File("src/main/java/eduPanel/uploads/" + oldPicturePath);
            if (oldFile.exists()) oldFile.delete();
            pictureRepository.delete(currentLecturer.getPicture());
        }

        // Remove old LinkedIn
        if (currentLecturer.getLinkedIn() != null) {
            linkedInRepository.delete(currentLecturer.getLinkedIn());
        }

        // Update lecturer data (except picture and LinkedIn)
        Lecturer newLecturer = transformer.fromLecturerReqTo(LecturerReqTo);
        newLecturer.setLinkedIn(null);
        newLecturer.setPicture(null);
        newLecturer = lecturerRepository.save(newLecturer);

        // Save new LinkedIn if present
        if (LecturerReqTo.getLinkedin() != null) {
            LinkedIn linkedIn = new LinkedIn(newLecturer, LecturerReqTo.getLinkedin());
            newLecturer.setLinkedIn(linkedInRepository.save(linkedIn));
        }

        // Save new picture to local uploads directory
        if (LecturerReqTo.getPicture() != null && !LecturerReqTo.getPicture().isEmpty()) {
            try {
                String picturePath = "lecturers/" + newLecturer.getId() + ".png";
                Path savePath = Paths.get("src/main/java/eduPanel/uploads", picturePath);
                Files.createDirectories(savePath.getParent());
                LecturerReqTo.getPicture().transferTo(savePath.toFile());

                Picture picture = new Picture(newLecturer, picturePath);
                newLecturer.setPicture(pictureRepository.save(picture));
            } catch (IOException e) {
                throw new AppException(500, "Failed to save the image locally", e);
            }
        }
    }

    @Override
    public void updateLecturerDetails(LectureTo LectureTo) {
        Lecturer currentLecturer = lecturerRepository.findById(LectureTo.getId()).orElseThrow(() -> new AppException(404, "No lecturer associated with the id"));

        /* Remove the old linked in */
        if (currentLecturer.getLinkedIn() != null) {
            linkedInRepository.delete(currentLecturer.getLinkedIn());
        }

        Lecturer newLecturer = transformer.fromLectureTo(LectureTo);
        newLecturer.setLinkedIn(null);

        newLecturer = lecturerRepository.save(newLecturer);

        /* Add a new linked in entry if exists */
        if (LectureTo.getLinkedin() != null) {
            LinkedIn linkedIn = new LinkedIn(newLecturer, LectureTo.getLinkedin());
            newLecturer.setLinkedIn(linkedInRepository.save(linkedIn));
        }
    }



    @Override
    public LectureTo getLecturerDetails(Integer lecturerId) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerId);
        if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");

        LectureTo lectureTo = transformer.toLectureTo(optLecturer.get());

        if (optLecturer.get().getPicture() != null) {
            String pictureUrl = "http://localhost:8080/uploads/" + optLecturer.get().getPicture().getPicturePath();
            lectureTo.setPicture(pictureUrl);
        }

        return lectureTo;
    }


    @Override
    public List<LectureTo> getLecturers(LecturerType type) {
        List<Lecturer> lecturerList;

        if (type != null) {
            lecturerList = lecturerRepository.findLecturersByType(type);
        } else {
            lecturerList = lecturerRepository.findAll();
        }

        return lecturerList.stream().map(l -> {
            LectureTo lectureTo = transformer.toLectureTo(l);
            if (l.getPicture() != null) {
                String pictureUrl = "http://localhost:8080/uploads/" + l.getPicture().getPicturePath();
                lectureTo.setPicture(pictureUrl);
            }
            return lectureTo;
        }).collect(Collectors.toList());
    }




}
