package com.snackgame.server.rank.domain;

import org.springframework.beans.factory.annotation.Value;

public interface BestScoreWithRankAndOwner {

    long getRank();

    int getScore();

    @Value("#{target.season_id}")
    long getSeasonId();

    @Value("#{target.owner_id}")
    Long getOwnerId();

    @Value("#{target.owner_name}")
    String getOwnerName();

    @Value("#{target.owner_level}")
    Long getOwnerLevel();

    @Value("#{target.owner_profile_image}")
    String getOwnerProfileImage();

    @Value("#{target.owner_group_id}")
    Long getOwnerGroupId();

    @Value("#{target.owner_group_name}")
    String getOwnerGroupName();
}
