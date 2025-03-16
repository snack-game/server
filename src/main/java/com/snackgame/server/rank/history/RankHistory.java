package com.snackgame.server.rank.history;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "rank_history")
@Getter
public class RankHistory {

    private Long ownerId;
    private Long beforeRank = Long.MAX_VALUE;
    private Long currentRank = Long.MAX_VALUE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RankHistory(Long ownerId, Long beforeRank, Long currentRank, Long id) {
        this.ownerId = ownerId;
        this.beforeRank = beforeRank;
        this.currentRank = currentRank;

        this.id = id;
    }

    public RankHistory renewWith(Long ownerId, Long newRank) {
        return new RankHistory(ownerId, currentRank, newRank, id);
    }

    public boolean canRenewBy(Long newRank) {
        return currentRank > newRank;

    }

}
