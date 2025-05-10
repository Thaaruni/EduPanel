package eduPanel.store;


import jakarta.persistence.EntityManager;


public class AppStore {

    private static final ThreadLocal<EntityManager> emStore = new ThreadLocal<>();

    public static EntityManager getEntityManager() {
        return emStore.get();
    }

    public static void setEntityManager(EntityManager entityManager) {
        emStore.set(entityManager);
    }
}
