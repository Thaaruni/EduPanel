package eduPanel.service.custom.impl;




import com.google.cloud.storage.Bucket;
import eduPanel.WebAppConfig;
import eduPanel.WebRootConfig;
import eduPanel.service.ServiceFactory;
import eduPanel.service.custom.LecturerService;
import eduPanel.store.AppStore;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import eduPanel.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
//@ExtendWith(MockitoExtension.class)
class LecturerServiceImplTest {

    private LecturerService lecturerService;
    @Autowired
    private EntityManagerFactory emf;


    private EntityManager entityManager;

//    @Mock
//    private LecturerRepository lecturerRepository;
//    @Mock
//    private LinkedInRepository linkedInRepository;
//    @Mock
//    private PictureRepository pictureRepository;

    @BeforeEach
    void setUp() {
        entityManager = emf.createEntityManager();
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);

//        when(lecturerRepository.save(any(Lecturer.class))).thenAnswer(inv ->{
//            Lecturer lecturer = inv.getArgument(0);
//            lecturer.setId(1);
//            return lecturer;
//        });
//
//        when(linkedInRepository.save(any(LinkedIn.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        lecturerService.setLecturerRepository(lecturerRepository);
//        lecturerService.setLinkedInRepository(linkedInRepository);
//        lecturerService.setPictureRepository(pictureRepository);
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void saveLecturer() {
        LecturerReqTo LecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BSc, MSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");
        LectureTo LectureTo = lecturerService.saveLecturer(LecturerReqTo);

        assertNotNull(LectureTo.getId());
        assertTrue(LectureTo.getId() > 0);
        assertEquals(LecturerReqTo.getName(), LectureTo.getName());
        assertEquals(LecturerReqTo.getDesignation(), LectureTo.getDesignation());
        assertEquals(LecturerReqTo.getQualification(), LectureTo.getQualification());
        assertEquals(LecturerReqTo.getType(), LectureTo.getType());
        assertEquals(LecturerReqTo.getDisplayOrder(), LectureTo.getDisplayOrder());
        assumingThat(LecturerReqTo.getLinkedin() != null, ()-> assertEquals(LecturerReqTo.getLinkedin(), LectureTo.getLinkedin()));
        assumingThat(LecturerReqTo.getLinkedin() == null, ()-> assertNull(LectureTo.getLinkedin()));

//        if (LecturerReqTo.getLinkedin() != null){
//            assertEquals(LecturerReqTo.getLinkedin(), LectureTo.getLinkedin());
//        }else{
//            assertNull(LectureTo.getLinkedin());
//        }
    }
}
