package com.snackgame.server.rank.domain

import org.springframework.beans.factory.annotation.Value

interface BestScoreWithRankAndOwner {
    val rank: Long

    @get:Value("#{target.owner_id}")
    val ownerId: Long

    @get:Value("#{target.game_id}")
    val gameId: Long

    @get:Value("#{target.season_id}")
    val seasonId: Long

    val score: Int

    @get:Value("#{target.owner_name}")
    val ownerName: String

    @get:Value("#{target.owner_level}")
    val ownerLevel: Long

    @get:Value("#{target.owner_profile_image}")
    val ownerProfileImage: String

    @get:Value("#{target.owner_group_id}")
    val ownerGroupId: Long?

    @get:Value("#{target.owner_group_name}")
    val ownerGroupName: String?
}
