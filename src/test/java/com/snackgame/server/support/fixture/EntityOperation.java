package com.snackgame.server.support.fixture;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface EntityOperation {

    void executeWith(EntityManager entityManager);
}
