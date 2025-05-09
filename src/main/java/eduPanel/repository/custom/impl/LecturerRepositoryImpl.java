package eduPanel.repository.custom.impl;



import eduPanel.entity.Lecturer;
import eduPanel.entity.SuperEntity;
import eduPanel.repository.custom.LecturerRepository;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class LecturerRepositoryImpl implements LecturerRepository {

    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public SuperEntity save(SuperEntity entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void update(SuperEntity entity) {
        em.merge(entity);
    }

    @Override
    public void deleteById(Serializable pk) {
        em.remove(em.find(Lecturer.class, pk));
    }

    @Override
    public boolean existsById(Serializable pk) {
        return em.find(Lecturer.class, pk) != null;
    }

    @Override
    public Optional<SuperEntity> findById(Serializable pk) {
        return Optional.ofNullable(em.find(Lecturer.class, pk));
    }

    @Override
    public List<SuperEntity> findAll() {
        return em.createQuery("SELECT l FROM Lecturer l").getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(l) FROM Lecturer l", Long.class).getSingleResult();
    }
}