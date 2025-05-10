package eduPanel.repository.custom.impl;


import  eduPanel.entity.Lecturer;
import  eduPanel.repository.CrudRepositoryImpl;
import  eduPanel.repository.custom.LecturerRepository;

import java.util.List;



public class LecturerRepositoryImpl extends CrudRepositoryImpl<Lecturer, Integer> implements LecturerRepository {

    @Override
    public List<Lecturer> findFullTimeLecturers() {
        return getEntityManager().createQuery("SELECT l FROM Lecturer l WHERE l.type = eduPanel.util.LecturerType.FULL_TIME", Lecturer.class).getResultList();
    }

    @Override
    public List<Lecturer> findVisitingLectures() {
        return getEntityManager().createQuery("SELECT l FROM Lecturer l WHERE l.type = eduPanel.util.LecturerType.VISITING", Lecturer.class).getResultList();
    }
}