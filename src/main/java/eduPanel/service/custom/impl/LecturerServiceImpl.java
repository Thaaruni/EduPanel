package eduPanel.service.custom.impl;


import eduPanel.entity.Lecturer;
import eduPanel.entity.Picture;
import eduPanel.exception.AppException;
import eduPanel.repository.LecturerRepository;
import eduPanel.repository.LinkedInRepository;
import eduPanel.repository.PictureRepository;
import eduPanel.service.custom.LecturerService;
import eduPanel.service.util.Transformer;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
    public void updateLecturerDetails(LecturerReqTo lecturerReqTO) {

    }



    @Override
    public LectureTo saveLecturer(LecturerReqTo lecturerReqTO) {
        Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
        lecturerRepository.save(lecturer);

        if (lecturerReqTO.getLinkedin() != null) {
            linkedInRepository.save(lecturer.getLinkedIn());
        }

        String pictureUrl = null;
        if (lecturerReqTO.getPicture() != null && !lecturerReqTO.getPicture().isEmpty()) {
            String picturePath = "lecturers/" + lecturer.getId() + ".png";
            Path savePath = Paths.get("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-EduPanel/edupanel-jave-api/src/main/java/eduPanel/uploads", picturePath); // Change this to your actual upload location

            try {
                Files.createDirectories(savePath.getParent());
                lecturerReqTO.getPicture().transferTo(savePath.toFile());
            } catch (IOException e) {
                throw new AppException(500, "Failed to upload the image", e);
            }

            Picture picture = new Picture(lecturer, picturePath);
            pictureRepository.save(picture);
            pictureUrl = "/uploads/" + picturePath; // This assumes Spring is serving static files from /uploads
        }

        LectureTo lecturerTO = transformer.toLecturerTO(lecturer);
        lecturerTO.setPicture(pictureUrl);
        return lecturerTO;
    }

}
