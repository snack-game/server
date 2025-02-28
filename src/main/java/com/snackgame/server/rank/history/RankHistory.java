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

    private Long beforeRank;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RankHistory(Long ownerId, Long beforeRank, Long id) {
        this.ownerId = ownerId;
        this.beforeRank = beforeRank;
        this.id = id;
    }

    public RankHistory renewWith(Long ownerId, Long newRank) {
        return new RankHistory(ownerId, newRank, id);
    }

    public boolean canRenewBy(Long rank) {
        return beforeRank > rank;
    }

}
