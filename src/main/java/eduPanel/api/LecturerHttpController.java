package eduPanel.api;

import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.to.request.LecturerReqTo;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {
    @Autowired
    private EntityManager em;
    @Autowired
    private ModelMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data" , produces = "application/json")
    public void createNewLecturer(@ModelAttribute @Validated(LecturerReqTo.Create.class)
                                      LecturerReqTo lecturerReqTo) {
        em.getTransaction().begin();
        try{
            Lecturer lecturer = mapper.map(lecturerReqTo , Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);

            if(lecturerReqTo.getLinkedin() != null) {
                em.persist(new LinkedIn(lecturer , lecturerReqTo.getLinkedin()) );
            }

            if(lecturerReqTo.getPicture() != null) {
                Picture picture = new Picture(lecturer , "lectures/" + lecturer.getId());
                em.persist(picture);
            }
            em.getTransaction().commit();
        }catch(Throwable e){
            em.getTransaction().rollback();
            throw e;
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
