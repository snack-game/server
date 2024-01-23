package com.snackgame.server.support.restassured;

import java.util.Objects;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;

@Component
public class SetupRestAssuredPort implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Integer port = getEnvironmentFrom(context).getProperty("local.server.port", Integer.class);
        if (!Objects.isNull(port)) {
            RestAssured.port = port;
            return;
        }
        throw new IllegalArgumentException("사용중인 포트를 찾지 못했습니다");
    }

    private Environment getEnvironmentFrom(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context)
                .getBean(Environment.class);
    }
}
