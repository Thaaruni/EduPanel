package eduPanel.repository;

import jakarta.persistence.EntityManager;

public interface SuperRepository {
    void setEntityManager(EntityManager em);
}