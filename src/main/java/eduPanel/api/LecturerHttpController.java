package eduPanel.api;

import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    @PatchMapping(value = "/{lecturer-id}" , consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId){
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}" , consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId){
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){

    }

    @GetMapping(produces = "application/json")
    public void getAllLecturers(){

    }

    @GetMapping(value = "/{lecturer-id}" , produces ="application/json")
    public void getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){

    }

    @GetMapping(params = "type=full-time" , produces = "application/json" )
    public void getFullTimeLecturer(){

    }

    @GetMapping(params = "type=visiting" , produces = "application/json")
    public void getVisitingLecturer(){

    }
}
