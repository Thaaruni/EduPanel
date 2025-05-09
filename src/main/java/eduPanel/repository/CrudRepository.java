package eduPanel.repository;

import eduPanel.entity.SuperEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;


public interface CrudRepository<T extends SuperEntity, ID extends Serializable> extends SuperRepository {

    EntityManager getEntityManager();

    T save(T entity);

    void update(T entity);

    void deleteById(ID pk);

    boolean existsById(ID pk);

    Optional<T> findById(ID pk);

    List<T> findAll();

    long count();
}