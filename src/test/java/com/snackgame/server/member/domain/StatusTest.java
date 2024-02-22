package com.snackgame.server.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StatusTest {

    @ParameterizedTest
    @CsvSource({"200,1", "440,2", "728,3", "1073.6,4"})
    void 레벨업_한다(double expAmount, int expectedLevel) {
        Status status = new Status();

        status.addExp(expAmount);
        assertThat(status.getLevel()).isEqualTo(expectedLevel);
    }
}
