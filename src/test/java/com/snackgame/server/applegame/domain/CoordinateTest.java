package com.snackgame.server.applegame.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.exception.NegativeCoordinateException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CoordinateTest {

    @Test
    void 음수_축좌표는_예외를_던진다() {
        assertThatThrownBy(() -> new Coordinate(-1, 0))
                .isInstanceOf(NegativeCoordinateException.class);
    }
}
