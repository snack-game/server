package com.snackgame.server.rank.history;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.snackgame.server.rank.event.BestScoreRenewalEvent;
import com.snackgame.server.rank.provoke.ProvokeService;

@Service
public class RankHistoryRenewal {

    private final RankHistories rankHistories;
    private final ProvokeService provokeService;

    public RankHistoryRenewal(RankHistories rankHistories, ProvokeService provokeService) {
        this.rankHistories = rankHistories;
        this.provokeService = provokeService;

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void renewHistoryWith(BestScoreRenewalEvent event) {
        RankHistory rankHistory = getOrCreateRankHistoryBy(event);
        if (rankHistory.canRenewBy(event.getRenewedRank())) {
            rankHistories.save(rankHistory.renewWith(event.getOwnerId(), event.getRenewedRank()));
            updateAndReserve(event);
        }

    }

    private void updateAndReserve(BestScoreRenewalEvent event) {
        rankHistories.update(event.getOwnerId(), event.getRenewedRank());
        provokeService.reserveProvoking(event.getOwnerId(), event.getSessionId());
    }

    private RankHistory getOrCreateRankHistoryBy(BestScoreRenewalEvent event) {
        RankHistory rankHistory = rankHistories.findByOwnerId(event.getOwnerId());
        if (rankHistory != null) {
            return rankHistory;
        }
        return rankHistories.save(new RankHistory(event.getOwnerId(), event.getRenewedRank()));
    }

}
