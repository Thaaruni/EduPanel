package eduPanel.service.customImpl;

import eduPanel.repository.LecturerRepository;
import eduPanel.repository.LinkedInRepository;
import eduPanel.repository.PictureRepository;
import eduPanel.service.custom.LecturerService;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import eduPanel.util.LecturerType;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;




import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@Transactional
class LecturerServiceImplTest {

    @Autowired
    private LecturerService lectureService;



//    @Mock
//    private LecturerRepository lecturerRepository;
//    @Mock
//    private LinkedInRepository linkedInRepository;
//    @Mock
//    private PictureRepository pictureRepository;

    @BeforeEach
    void setUp() {

//        entityManager = emf.createEntityManager();

        // when(lecturerRepository.count()).thenReturn(10L);
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
//        entityManager.close();
    }

    @Test
    void saveLecturer() {
        LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BSc, MSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");
        LectureTo lecturerTO = lecturerService.saveLecturer(lecturerReqTo);

        assertNotNull(lecturerTO.getId());
        assertTrue(lecturerTO.getId() > 0);
        assertEquals(lecturerReqTo.getName(), lecturerTO.getName());
        assertEquals(lecturerReqTo.getDesignation(), lecturerTO.getDesignation());
        assertEquals(lecturerReqTo.getQualification(), lecturerTO.getQualification());
        assertEquals(lecturerReqTo.getType(), lecturerTO.getType());
        assertEquals(lecturerReqTo.getDisplayOrder(), lecturerTO.getDisplayOrder());
        assumingThat(lecturerReqTo.getLinkedin() != null, () -> assertEquals(lecturerReqTo.getLinkedin(), lecturerTO.getLinkedin()));
        assumingThat(lecturerReqTo.getLinkedin() == null, () -> assertNull(lecturerTO.getLinkedin()));


    }
}