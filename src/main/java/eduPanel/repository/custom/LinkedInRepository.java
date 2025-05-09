package eduPanel.repository.custom;

import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.repository.CrudRepository;

import java.util.List;

public interface LinkedInRepository {

    LinkedIn saveLinkedIn(LinkedIn linkedIn);

    void updateLinkedIn(LinkedIn linkedIn);

    void deleteLinkedInByLecturer(Lecturer lecturer);

    boolean existsLinkedInByLecturer(Lecturer lecturer);

    LinkedIn findLinkedInByLecturer(Lecturer lecturer);

    List<LinkedIn> findAllLinkedIns();

    long countLinkedIns();
}

