package eduPanel.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;



@Component
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    private EntityManager em;

}
