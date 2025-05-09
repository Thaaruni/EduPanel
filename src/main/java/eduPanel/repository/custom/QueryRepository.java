package eduPanel.repository.custom;


import eduPanel.repository.SuperRepository;
import jakarta.persistence.EntityManager;

public interface QueryRepository extends SuperRepository {
    public default void setEntityManager(EntityManager em){

    }

}
