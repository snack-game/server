package com.snackgame.server.member.business.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.member.business.exception.GuestRestrictedException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GuestTest {

    @Test
    void 게스트는_이름을_변경할_수_없다() {
        assertThatThrownBy(() -> new Guest(new Name("게스트")).changeNameTo(new Name("이름")))
                .isInstanceOf(GuestRestrictedException.class);
    }

    @Test
    void 게스트는_그룹을_변경할_수_없다() {
        assertThatThrownBy(() -> new Guest(new Name("게스트")).changeGroupTo(new Group("그룹")))
                .isInstanceOf(GuestRestrictedException.class);
    }

    @Test
    void 게스트의_계정_타입은_GUEST이다() {
        assertThat(new Guest(new Name("게스트")).getAccountType()).isEqualTo(AccountType.GUEST);
    }
}
