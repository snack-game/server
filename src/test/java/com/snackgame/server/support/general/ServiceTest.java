package com.snackgame.server.support.general;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.snackgame.server.support.database.CleanDatabaseBeforeEach;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CleanDatabaseBeforeEach.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public @interface ServiceTest {

}
