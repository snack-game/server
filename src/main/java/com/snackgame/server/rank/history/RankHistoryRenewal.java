package com.snackgame.server.rank.history;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.snackgame.server.rank.event.BestScoreRenewalEvent;

@Service
public class RankHistoryRenewal {

    private final RankHistories rankHistories;

    public RankHistoryRenewal(RankHistories rankHistories) {
        this.rankHistories = rankHistories;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void renewHistoryWith(BestScoreRenewalEvent event) {

        RankHistory rankHistory = rankHistories.findByOwnerId(event.getOwnerId());
        if (rankHistory.canRenewBy(event.getRenewedRank())) {
            /// TODO: 1/16/25 save와 update가 필요한게 같은데 어떻게 잘 합쳐볼수없을까
            rankHistories.save(rankHistory.renewWith(event.getOwnerId(), event.getRenewedRank()));
            rankHistories.update(event.getOwnerId(), event.getRenewedRank());
        }
    }

}
