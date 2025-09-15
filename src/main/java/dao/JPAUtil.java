package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("baitap06");

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}