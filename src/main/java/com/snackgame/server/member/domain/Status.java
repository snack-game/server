package com.snackgame.server.member.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Getter
@Embeddable
public class Status {

    private static final double MULTIPLIER = 1.2;

    @Column(name = "level")
    private long level = 0;

    @Column(name = "exp")
    private BigDecimal exp = ZERO;

    public Status() {
    }

    public void addExp(double amount) {
        BigDecimal remainingExp = exp.add(BigDecimal.valueOf(amount));
        while (remainingExp.compareTo(expRequiredFor(level)) >= 0) {
            remainingExp = remainingExp.subtract(expRequiredFor(level));
            addLevel();
        }
        this.exp = remainingExp;
    }

    private BigDecimal expRequiredFor(Long level) {
        BigDecimal weight = BigDecimal.valueOf(MULTIPLIER).pow(level.intValue());
        return BigDecimal.valueOf(200).multiply(weight);
    }

    private void addLevel() {
        this.level += 1;
    }

}
