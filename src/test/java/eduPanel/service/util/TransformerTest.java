package eduPanel.service.util;




import eduPanel.WebAppConfig;
import eduPanel.WebRootConfig;
import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.to.LectureTo;
import eduPanel.util.LecturerType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
class TransformerTest {

    private final Transformer transformer = new Transformer();

    @Test
    void toLectureTo() {
        Lecturer lecturer = new Lecturer(2,
                "Nuwan",
                "Associate Trainer",
                "BSc in Computing",
                LecturerType.VISITING,
                5);
        lecturer.setLinkedIn(new LinkedIn(lecturer, "https://linkedin.com/nuwan"));
        LectureTo LectureTo = transformer.toLectureTo(lecturer);

        assertEquals(lecturer.getId(), LectureTo.getId());
        assertEquals(lecturer.getName(), LectureTo.getName());
        assertEquals(lecturer.getDesignation(), LectureTo.getDesignation());
        assertEquals(lecturer.getQualification(), LectureTo.getQualification());
        assertEquals(lecturer.getType(), LectureTo.getType());
        assertEquals(lecturer.getLinkedIn().getUrl(), LectureTo.getLinkedin());
    }
}