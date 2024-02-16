package com.snackgame.server.support.fixture;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Component;

@Component
public class FixtureSaver {

    private static FixtureSaver INSTANCE;

    private final EntityManagerFactory entityManagerFactory;

    public FixtureSaver(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        INSTANCE = this;
    }

    public static FixtureSaver getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalStateException("아직 인스턴스가 초기화되지 않았습니다");
    }

    private void execute(EntityOperation entityOperation) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        transaction.begin();
        entityOperation.executeWith(entityManager);
        transaction.commit();
        entityManager.close();
    }

    public static void save(Stream<Object> objects) {
        getInstance().execute(entityManager ->
                objects.forEach(entity -> {
                    validateJpaEntity(entity);
                    entityManager.merge(entity);
                })
        );
    }

    public static void save(Object... objects) {
        save(Stream.of(objects));
    }

    private static void validateJpaEntity(Object object) {
        if (!object.getClass().isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("저장할 수 없는 객체가 있습니다: " + object.getClass().getSimpleName());
        }
    }
}
