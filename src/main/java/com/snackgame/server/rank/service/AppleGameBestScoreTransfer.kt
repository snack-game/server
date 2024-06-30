package com.snackgame.server.rank.service

import com.snackgame.server.applegame.domain.game.BestScoreTransfer
import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.rank.domain.BestScores
import com.snackgame.server.rank.domain.SeasonRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AppleGameBestScoreTransfer(
    private val bestScores: BestScores,
    private val seasonRepository: SeasonRepository
) : BestScoreTransfer {

    @Transactional
    override fun transfer(victimMemberId: Long, currentMemberId: Long) {
        val gameMetadata = Metadata.entries
        val seasons = seasonRepository.findAll()
        gameMetadata.forEach { metadata ->
            seasons.forEach { transfer(metadata.gameId, it.id, victimMemberId, currentMemberId) }
        }
    }

    private fun transfer(gameId: Long, seasonId: Long, victimMemberId: Long, currentMemberId: Long) {
        val victimBestScore = bestScores.findByOwnerIdAndGameIdAndSeasonId(victimMemberId, gameId, seasonId)
        val currentBestScore = bestScores.findByOwnerIdAndGameIdAndSeasonId(currentMemberId, gameId, seasonId)

        if (victimBestScore == null) {
            return
        }
        if (currentBestScore == null || victimBestScore.beats(currentBestScore)) {
            bestScores.save(victimBestScore.transferTo(currentMemberId))
        }
    }
}
