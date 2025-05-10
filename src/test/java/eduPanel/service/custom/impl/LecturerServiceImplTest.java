package eduPanel.service.custom.impl;




import eduPanel.WebAppConfig;
import eduPanel.WebRootConfig;
import eduPanel.exception.AppException;
import eduPanel.service.ServiceFactory;
import eduPanel.service.custom.LecturerService;
import eduPanel.store.AppStore;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo ;
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

class LecturerServiceImplTest {

    private LecturerService lecturerService;

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = emf.createEntityManager();
        AppStore.setEntityManager(entityManager);
        lecturerService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void saveLecturer() {
        LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BSc, MSc", LecturerType.VISITING, 5,
                null, "https://linkedin.com");

        LectureTo lectureTo = lecturerService.saveLecturer(lecturerReqTo);

        assertNotNull(lectureTo.getId());
        assertTrue(lectureTo.getId() > 0);
        assertEquals(lecturerReqTo.getName(), lectureTo.getName());
        assertEquals(lecturerReqTo.getDesignation(), lectureTo.getDesignation());
        assertEquals(lecturerReqTo.getQualification(), lectureTo.getQualification());
        assertEquals(lecturerReqTo.getType(), lectureTo.getType());
        assertEquals(lecturerReqTo.getDisplayOrder(), lectureTo.getDisplayOrder());
        assumingThat(lecturerReqTo.getLinkedin() != null, () ->
                assertEquals(lecturerReqTo.getLinkedin(), lectureTo.getLinkedin()));
        assumingThat(lecturerReqTo.getLinkedin() == null, () ->
                assertNull(lectureTo.getLinkedin()));
    }

    @Test
    void deleteLecturer() {
        LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BSc, MSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");

        LectureTo lectureTo = lecturerService.saveLecturer(lecturerReqTo);
        lecturerService.deleteLecturer(lectureTo.getId());

        assertThrows(AppException.class, () -> lecturerService.getLecturerDetails(lectureTo.getId()));
        assertThrows(AppException.class, () -> lecturerService.deleteLecturer(-100));
    }

    @Test
    void getLecturerDetails() {
        LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BSc, MSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");

        LectureTo saved = lecturerService.saveLecturer(lecturerReqTo);
        LectureTo lecturer = lecturerService.getLecturerDetails(saved.getId());

        assertEquals(saved, lecturer);
        assertThrows(AppException.class, () -> lecturerService.getLecturerDetails(-100));
    }

    @Test
    void getLecturers() {
        for (int i = 0; i < 10; i++) {
            LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                    "Associate Lecturer", "BSc, MSc",
                    i < 5 ? LecturerType.VISITING : LecturerType.FULL_TIME,
                    5,
                    null,
                    "https://linkedin.com");
            lecturerService.saveLecturer(lecturerReqTo);
        }

        assertTrue(lecturerService.getLecturers(null).size() >= 10);
        assertTrue(lecturerService.getLecturers(LecturerType.FULL_TIME).size() >= 5);
        assertTrue(lecturerService.getLecturers(LecturerType.VISITING).size() >= 5);
    }

    @Test
    void updateLecturerDetails() {
        LecturerReqTo lecturerReqTo = new LecturerReqTo("Amith",
                "Associate Lecturer", "BScMSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");

        LectureTo lectureTo = lecturerService.saveLecturer(lecturerReqTo);
        lectureTo.setName("Nuwan");
        lectureTo.setLinkedin(null);
        lecturerService.updateLecturerDetails(lectureTo);

        LectureTo lecturer = lecturerService.getLecturerDetails(lectureTo.getId());
        assertEquals(lectureTo, lecturer);
    }
}
