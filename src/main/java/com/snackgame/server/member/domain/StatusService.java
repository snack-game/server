package com.snackgame.server.member.domain;

import org.springframework.stereotype.Service;

@Service
public class StatusService {

    public Status updateStatus(Status status, double score) {
        double remainExp = status.getExp() + score;
        double expRequireForLevel = calculateExpByLevel(status.getLevel());

        while (isExpExceeded(remainExp, expRequireForLevel)) {
            levelUp(status);
            remainExp -= expRequireForLevel;
            expRequireForLevel = calculateExpByLevel(status.getLevel());
        }

        status.changeExpTo(remainExp);
        return status;
    }

    private void levelUp(Status status) {
        status.addLevel(1);
    }

    private boolean isExpExceeded(double afterExp, double beforeExp) {
        return afterExp > beforeExp;
    }

    private double calculateExpByLevel(long level) {
        return Math.pow(1.2, level) * 200;
    }

}

