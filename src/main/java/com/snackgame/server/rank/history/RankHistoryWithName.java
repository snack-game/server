package com.snackgame.server.rank.history;

public interface RankHistoryWithName {
    Long getId();

    Long getOwnerId();

    Long getBeforeRank();

    Long getCurrentRank();

    String getName();
}
