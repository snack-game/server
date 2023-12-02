package com.snackgame.server.applegame.business.domain.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.snackgame.server.applegame.business.exception.AppleNumberRangeException;

@JsonDeserialize(using = AppleDeserializer.class)
public abstract class Apple {

    protected static final int NUMBER_MINIMUM = 1;
    protected static final int NUMBER_MAXIMUM = 9;

    private final int number;

    protected Apple() {
        this.number = 0;
    }

    protected Apple(int number) {
        validateRangeOf(number);
        this.number = number;
    }

    private void validateRangeOf(int number) {
        if (NUMBER_MINIMUM <= number && number <= NUMBER_MAXIMUM) {
            return;
        }
        throw new AppleNumberRangeException();
    }

    public abstract GoldenApple golden();

    @JsonProperty("isGolden")
    public abstract boolean isGolden();

    public abstract boolean exists();

    public int getNumber() {
        return number;
    }
}
