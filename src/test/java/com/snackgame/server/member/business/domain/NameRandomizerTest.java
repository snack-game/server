package com.snackgame.server.member.business.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NameRandomizerTest {

    private final NameRandomizer nameRandomizer = new AlphabetNameRandomizer();

    @Test
    void 이름앞에_접두사가_붙는다() {
        var name = nameRandomizer.get();

        assertThat(name).startsWith("게스트#");
    }

    @Test
    void 이름의_8자리를_무작위로_생성한다() {
        var name = nameRandomizer.get();

        assertThat(name.substring(4)).hasSize(8);
    }
}
