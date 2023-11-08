package com.snackgame.server.applegame.business.rank;

import org.springframework.beans.factory.annotation.Value;

public interface BestScoreWithRankAndOwner {

    long getRank();

    int getScore();

    @Value("#{target.owner_id}")
    Long getOwnerId();

    @Value("#{target.owner_name}")
    String getOwnerName();

    @Value("#{target.owner_group_id}")
    Long getOwnerGroupId();

    @Value("#{target.owner_group_name}")
    String getOwnerGroupName();
}
