package com.snackgame.server.applegame.fixture;

import java.util.List;

import com.snackgame.server.applegame.domain.Apple;
import com.snackgame.server.applegame.domain.Board;

public class TestFixture {

    /**
     * @summary <p>[1, 1, _, 5]</p>
     * <p>[9, 2, _, 2]</p>
     */
    public static Board TWO_BY_FOUR() {
        return new Board(List.of(
                List.of(new Apple(1), new Apple(1), Apple.EMPTY, new Apple(5)),
                List.of(new Apple(9), new Apple(2), Apple.EMPTY, new Apple(2))
        ));
    }
}
