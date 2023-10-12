package com.snackgame.server.member.fixture;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.Id;

public abstract class FixtureUtil {

    // 2단계 이상 상속된 필드들은 지원하지 않는다.
    public static <T> T idIgnored(T object) {
        try {
            Field id = Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(it -> it.isAnnotationPresent(Id.class))
                    .findFirst()
                    .or(() -> Arrays.stream(object.getClass().getSuperclass().getDeclaredFields())
                            .filter(it -> it.isAnnotationPresent(Id.class))
                            .findFirst()
                    )
                    .get();
            id.setAccessible(true);
            id.set(object, null);
            return object;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T pushing(Object value, T target) {
        try {
            Field targetField = Arrays.stream(target.getClass().getDeclaredFields())
                    .filter(it -> it.getType().isAssignableFrom(value.getClass()))
                    .findFirst()
                    .get();
            targetField.setAccessible(true);
            targetField.set(target, value);
            return target;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
