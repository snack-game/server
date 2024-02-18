package com.snackgame.server.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Status {

    @Column(name = "level")
    private long level = 0;

    @Column(name = "exp")
    private double exp = 0;

    public Status() {
    }

    public long getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public long addLevel(long level) {
        return this.level += level;
    }

    public void changeExpTo(double exp) {
        this.exp = exp;
    }

}
