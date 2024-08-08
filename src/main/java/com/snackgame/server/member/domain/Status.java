package com.snackgame.server.member.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.stream.LongStream;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Getter
@Embeddable
public class Status {

    private static final double MULTIPLIER = 1.2;

    @Column(name = "level", nullable = false)
    private Long level = 0L;

    @Column(name = "exp", nullable = false)
    private BigDecimal exp = ZERO;

    public Status() {
    }

    public Status(long level, double exp) {
        this.level = level;
        this.exp = BigDecimal.valueOf(exp);
    }

    public void addExp(double amount) {
        BigDecimal remainingExp = exp.add(BigDecimal.valueOf(amount));
        while (remainingExp.compareTo(expRequiredForLevel()) >= 0) {
            remainingExp = remainingExp.subtract(expRequiredForLevel());
            addLevel();
        }
        this.exp = remainingExp;
    }

    public BigDecimal expRequiredForLevel() {
        return getExpRequiredFor(level);
    }

    private BigDecimal getExpRequiredFor(Long level) {
        BigDecimal weight = BigDecimal.valueOf(MULTIPLIER).pow(level.intValue());
        return BigDecimal.valueOf(200).multiply(weight);
    }

    private void addLevel() {
        this.level += 1;
    }

    public BigDecimal getExp() {
        return exp;
    }

    public double getTotalExp() {
        double earnedBefore = LongStream.range(0, level)
                .mapToDouble(level -> getExpRequiredFor(level).doubleValue())
                .sum();
        return exp.add(BigDecimal.valueOf(earnedBefore)).doubleValue();
    }

    public void reset() {
        this.level = 0L;
        this.exp = ZERO;
    }
}
