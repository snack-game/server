package com.snackgame.server.rank.controller

import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.member.domain.Member
import com.snackgame.server.rank.controller.dto.RankResponseV2
import com.snackgame.server.rank.domain.Season
import com.snackgame.server.rank.domain.SeasonRepository
import com.snackgame.server.rank.history.RankHistory
import com.snackgame.server.rank.history.RankHistoryService
import com.snackgame.server.rank.service.BestScoreRankService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "⭐️ 랭킹")
@RestController
class RankingController(
    private val bestScoreRankService: BestScoreRankService,
    private val seasonRepository: SeasonRepository,
    private val rankHistoryService: RankHistoryService
) {

    @Operation(summary = "전체 시즌 - 선두 랭크 조회", description = "전체 시즌에서 랭킹을 선두 50등까지 조회한다")
    @GetMapping("/rankings/{gameId}")
    fun showLeadingRanksBy(
        @PathVariable gameId: Long,
        @RequestParam("by") criteria: Criteria
    ): List<RankResponseV2> {
        return bestScoreRankService.rankLeadersBy(gameId)
    }

    @Operation(summary = "전체 시즌 - 자신의 랭크 조회", description = "전체 시즌에서 자신의 랭킹을 조회한다")
    @GetMapping("/rankings/{gameId}/me")
    fun showRankOf(
        @Authenticated member: Member,
        @PathVariable gameId: Long,
        @RequestParam("by") criteria: Criteria
    ): RankResponseV2 {
        return bestScoreRankService.rank(member.id, gameId)
    }

    @Operation(summary = "선두 랭크 조회", description = "특정 시즌에서 랭킹을 선두 50등까지 조회한다")
    @GetMapping("/rankings/{gameId}/{seasonId}")
    fun showLeadingRanksBy(
        @PathVariable seasonId: Long,
        @PathVariable gameId: Long,
        @RequestParam("by") criteria: Criteria
    ): List<RankResponseV2> {
        return bestScoreRankService.rankLeadersBy(gameId, seasonId)
    }

    @Operation(summary = "자신의 랭크 조회", description = "특정 시즌에서 자신의 랭킹을 조회한다")
    @GetMapping("/rankings/{gameId}/{seasonId}/me")
    fun showRankOf(
        @Authenticated member: Member,
        @PathVariable seasonId: Long,
        @PathVariable gameId: Long,
        @RequestParam("by") criteria: Criteria
    ): RankResponseV2 {
        return bestScoreRankService.rank(member.id, gameId, seasonId)
    }

    @Operation(summary = "모든 시즌 조회", description = "지금까지 있었던 모든 시즌들을 조회한다")
    @GetMapping("/seasons")
    fun showAllSeasons(): List<Season> {
        return seasonRepository.findAll()
    }

    @GetMapping("/rankings/histories")
    fun showMembersBelow(
        @Authenticated member: Member
    ): List<RankHistory> {
        return rankHistoryService.findMemberBelow(member.id)
    }

    enum class Criteria {
        BEST_SCORE
    }
}
