package eduPanel.api;

import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {
    @Autowired
    private EntityManager em;
    @Autowired
    private ModelMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody LectureTo createNewLecturer(
            @ModelAttribute @Validated(LecturerReqTo.Create.class) LecturerReqTo lecturerReqTo)  {
        em.getTransaction().begin();
        try{
            Lecturer lecturer = mapper.map(lecturerReqTo , Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);
            LectureTo lectureTo = mapper.map(lecturer , LectureTo.class);

            if(lecturerReqTo.getLinkedin() != null) {
                em.persist(new LinkedIn(lecturer , lecturerReqTo.getLinkedin()) );
             lectureTo.setLinkedin(lecturerReqTo.getLinkedin());
            }

//            if(lecturerReqTo.getPicture() != null) {
//                em.persist(new Picture(lecturer , lecturerReqTo.getPicture()) );
//                lectureTo.setPicture(lecturerReqTo.getPicture());
//            }

            if (lecturerReqTo.getPicture() != null && !lecturerReqTo.getPicture().isEmpty()) {
                String picturePath = "lecturers/" + lecturer.getId() + ".png";
                Path savePath = Paths.get("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-EduPanel/edupanel-jave-api/src/main/java/eduPanel/uploads", picturePath);
                Files.createDirectories(savePath.getParent());
                lecturerReqTo.getPicture().transferTo(savePath.toFile());

                Picture picture = new Picture(lecturer, picturePath);
                em.persist(picture);

                // Set URL so it's accessible via browser/Postman
                lectureTo.setPicture("http://localhost:8080/uploads/" + picturePath);
            }



            System.out.println(lectureTo);
            em.getTransaction().commit();
            return lectureTo;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace(); // Log the stack trace (optional: use a logger instead)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create lecturer", e);
        }

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(
            @PathVariable("lecturer-id") Integer lecturerId,
            @ModelAttribute @Validated(LecturerReqTo.Update.class) LecturerReqTo lecturerReqTo) {

        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if (currentLecturer == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found");

        em.getTransaction().begin();
        try {
            Lecturer newLecturer = mapper.map(lecturerReqTo, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(null);
            newLecturer.setLinkedIn(null);

            if(lecturerReqTo.getLinkedin() != null) {
                newLecturer.setLinkedIn(new LinkedIn(newLecturer , lecturerReqTo.getLinkedin() ));
            }
            if(lecturerReqTo.getPicture() != null) {
                newLecturer.setPicture(new Picture(newLecturer , "lecturers/"+ lecturerId ));
            }

            if(newLecturer.getLinkedIn() != null && currentLecturer.getLinkedIn() == null) {
                em.persist(newLecturer.getLinkedIn());
            }else if(newLecturer.getLinkedIn() == null && currentLecturer.getLinkedIn() != null) {
               em.remove(currentLecturer.getLinkedIn());
            }else if(newLecturer.getLinkedIn() != null) {
               em.merge(newLecturer.getLinkedIn());
            }

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                // Case 1: New picture is added, no old picture exists
                String picturePath = "lecturers/" + lecturerId + ".png";
                Path savePath = Paths.get("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-EduPanel/edupanel-jave-api/src/main/java/eduPanel/uploads/lecturers", picturePath);
                Files.createDirectories(savePath.getParent());
                lecturerReqTo.getPicture().transferTo(savePath.toFile());

                newLecturer.setPicture(new Picture(newLecturer, picturePath));
                em.persist(newLecturer.getPicture());

            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                // Case 2: Picture is removed
                Path oldPath = Paths.get("D:/edu-panel/uploads", currentLecturer.getPicture().getPicturePath());
                Files.deleteIfExists(oldPath);
                em.remove(currentLecturer.getPicture());

            } else if (newLecturer.getPicture() != null) {
                // Case 3: Picture is updated
                String picturePath = "lecturers/" + lecturerId + ".png";
                Path savePath = Paths.get("D:/edu-panel/uploads", picturePath);
                Files.createDirectories(savePath.getParent());
                lecturerReqTo.getPicture().transferTo(savePath.toFile());

                newLecturer.getPicture().setPicturePath(picturePath);
                em.merge(newLecturer.getPicture());
            }



            em.merge(newLecturer);
            em.getTransaction().commit();
        } catch (Throwable e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}" , consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId,
    @RequestBody @Validated LectureTo lecturerTo){
        Lecturer currentLecturer = em.find(Lecturer.class, lecturerId);
        if(currentLecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found");
        em.getTransaction().begin();
        try{
            Lecturer newLecturer = mapper.map(lecturerTo, Lecturer.class);
            newLecturer.setId(lecturerId);
            newLecturer.setPicture(currentLecturer.getPicture());
            newLecturer.setLinkedIn(lecturerTo.getLinkedin() != null ? new LinkedIn(newLecturer, lecturerTo.getLinkedin()) : null);
            if(currentLecturer.getLinkedIn() != null && newLecturer.getLinkedIn() == null) {
                em.remove(currentLecturer.getLinkedIn());
            }else if(currentLecturer.getLinkedIn() == null && newLecturer.getLinkedIn() != null) {
                em.persist(newLecturer.getLinkedIn());
            }else if(newLecturer.getLinkedIn() != null ) {
                em.merge(newLecturer.getLinkedIn());
            }
            em.merge(newLecturer);
            em.getTransaction().commit();
        }catch(Throwable e){
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId) {
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        em.getTransaction().begin();
        try {
            // Handle picture file deletion
            if (lecturer.getPicture() != null) {
                String relativePath = lecturer.getPicture().getPicturePath(); // e.g., "lecturers/19.png"
                Path picturePath = Paths.get("/home/thaaruni-dissanayake/Documents/DEP13/my_projects/last-project-EduPanel/edupanel-jave-api/src/main/java/eduPanel/uploads ", relativePath); // Change to your base path
                File pictureFile = picturePath.toFile();
                if (pictureFile.exists()) {
                    pictureFile.delete();
                }

                // Optionally remove the Picture entity
                em.remove(lecturer.getPicture());
            }

            // Remove LinkedIn entity if exists
            if (lecturer.getLinkedIn() != null) {
                em.remove(lecturer.getLinkedIn());
            }

            // Remove lecturer
            em.remove(lecturer);
            em.getTransaction().commit();
        } catch (Throwable e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }


    @GetMapping(produces = "application/json")
    public List<LectureTo> getAllLecturers(){
        //Use JPQL
        TypedQuery<Lecturer> query =  em.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLectureTOList(query);
    }

    @GetMapping(value = "/{lecturer-id}" , produces ="application/json")
    public LectureTo getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if(lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found");
        LectureTo lectureTo = mapper.map(lecturer, LectureTo.class);
        if(lecturer.getLinkedIn() != null) {lectureTo.setLinkedin(lecturer.getLinkedIn().getUrl() );}
        if (lecturer.getPicture() != null) {lectureTo.setPicture(lecturer.getPicture().getPicturePath() );}
        return lectureTo;

    }

    @GetMapping(params = "type=full-time" , produces = "application/json" )
    public List<LectureTo> getFullTimeLecturer(){
        TypedQuery<Lecturer> query =  em.createQuery("SELECT l FROM Lecturer l WHERE l.type = eduPanel.util.LecturerType.FULL_TIME", Lecturer.class);
        return getLectureTOList(query);
    }

    @GetMapping(params = "type=visiting" , produces = "application/json")
    public List<LectureTo> getVisitingLecturer(){
        TypedQuery<Lecturer> query =  em.createQuery("SELECT l FROM Lecturer l WHERE l.type = eduPanel.util.LecturerType.VISITING", Lecturer.class);
        return getLectureTOList(query);
    }

    private List<LectureTo> getLectureTOList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(lectureEntity ->{
            LectureTo lectureTo = mapper.map(lectureEntity, LectureTo.class);
            if(lectureEntity.getLinkedIn() != null) {lectureTo.setLinkedin(lectureEntity.getLinkedIn().getUrl() );}
            if (lectureEntity.getPicture() != null) {lectureTo.setPicture(lectureEntity.getPicture().getPicturePath() );}
            return lectureTo;
        }).collect(Collectors.toList());
    }
}
