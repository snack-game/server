package com.snackgame.server.support.general;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.snackgame.server.support.database.CleanDatabaseBeforeEach;
import com.snackgame.server.support.fixture.FixtureSaver;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CleanDatabaseBeforeEach.class)
@DataJpaTest(excludeAutoConfiguration = TestDatabaseAutoConfiguration.class)
@Import(FixtureSaver.class)
public @interface DatabaseCleaningDataJpaTest {

}
