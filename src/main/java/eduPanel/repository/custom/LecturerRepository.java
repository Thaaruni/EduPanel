package eduPanel.repository.custom;

import eduPanel.entity.Lecturer;
import eduPanel.repository.CrudRepository;

import java.util.List;

public interface LecturerRepository extends CrudRepository<Lecturer, Integer> {
    List<Lecturer> findFullTimeLecturers();
    List<Lecturer> findVisitingLectures();
}
