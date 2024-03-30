package com.snackgame.server.applegame.domain.game;

import com.snackgame.server.applegame.exception.InaccuratePercentileException;

public class Percentile {

    private final double percentile;

    public Percentile(double percentile) {
        validateRangeOf(percentile);
        this.percentile = percentile;
    }

    private void validateRangeOf(double percentile) {
        if (percentile < 0 || 1 < percentile) {
            throw new InaccuratePercentileException(percentile);
        }
    }

    public int percentage() {
        double percentageWise = Math.floor(percentile * 100);
        return (int)percentageWise;
    }
}
