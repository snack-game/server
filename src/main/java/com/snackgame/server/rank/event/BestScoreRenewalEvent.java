package com.snackgame.server.rank.event;

import com.snackgame.server.game.metadata.Metadata;
import com.snackgame.server.game.session.event.SessionEndEvent;

import lombok.Getter;

@Getter
public class BestScoreRenewalEvent {

    private final Metadata metadata;

    private final Long ownerId;

    private final Long sessionId;

    private final Long renewedRank;

    public BestScoreRenewalEvent(Metadata metadata, Long ownerId, Long sessionId, Long renewedRank) {
        this.metadata = metadata;
        this.ownerId = ownerId;
        this.sessionId = sessionId;
        this.renewedRank = renewedRank;
    }

    public static BestScoreRenewalEvent of(SessionEndEvent sessionEndEvent, Long renewedRank) {
        return new BestScoreRenewalEvent(
                sessionEndEvent.getMetadata(),
                sessionEndEvent.getOwnerId(),
                sessionEndEvent.getSessionId(),
                renewedRank
        );
    }
}
