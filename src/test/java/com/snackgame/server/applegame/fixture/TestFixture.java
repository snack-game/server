package com.snackgame.server.applegame.fixture;

import java.util.List;

import com.snackgame.server.applegame.domain.apple.EmptyApple;
import com.snackgame.server.applegame.domain.apple.PlainApple;
import com.snackgame.server.applegame.domain.game.AppleGameBoard;

public class TestFixture {

    public static final String TWO_BY_FOUR_AS_JSON = "{\"apples\":[[{\"number\":1,\"isGolden\":false},{\"number\":1,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":5,\"isGolden\":false}],[{\"number\":9,\"isGolden\":false},{\"number\":2,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":2,\"isGolden\":false}]]}";
    public static final String TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON = "{\"apples\":[[{\"number\":1,\"isGolden\":false},{\"number\":8,\"isGolden\":false}],[{\"number\":9,\"isGolden\":true},{\"number\":2,\"isGolden\":false}]]}";

    /**
     * <p>[1, 1, _, 5]</p>
     * <p>[9, 2, _, 2]</p>
     */
    public static AppleGameBoard TWO_BY_FOUR() {
        return new AppleGameBoard(List.of(
                List.of(PlainApple.of(1), PlainApple.of(1), EmptyApple.get(), PlainApple.of(5)),
                List.of(PlainApple.of(9), PlainApple.of(2), EmptyApple.get(), PlainApple.of(2))
        ));
    }

    /**
     * <p>[1, 1]</p>
     * <p>[<b>9</b>, 2]</p>
     * 9는 황금사과이다.
     */
    public static AppleGameBoard TWO_BY_TWO_WITH_GOLDEN_APPLE() {
        return new AppleGameBoard(List.of(
                List.of(PlainApple.of(1), PlainApple.of(8)),
                List.of(PlainApple.of(9).golden(), PlainApple.of(2))
        ));
    }
}
