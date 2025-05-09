package eduPanel.repository.custom.impl;

import eduPanel.repository.custom.QueryRepository;
import jakarta.persistence.EntityManager;

public class QueryRepositoryImpl implements QueryRepository {
    private EntityManager em;

    @Override
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}