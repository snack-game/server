package com.snackgame.server.applegame.domain.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snackgame.server.applegame.exception.NoSuchSessionException;

@Repository
public interface AppleGames extends JpaRepository<AppleGame, Long> {

    @Modifying
    @Query("update AppleGame set ownerId = :toMemberId where ownerId = :fromMemberId")
    void transferAll(Long fromMemberId, Long toMemberId);

    default AppleGame getBy(Long sessionId) {
        return findById(sessionId).orElseThrow(NoSuchSessionException::new);
    }
}
