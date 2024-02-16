package com.snackgame.server.applegame.domain.game;

import java.util.Optional;

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

    @Deprecated(forRemoval = true)
    default AppleGame getBy(Long sessionId) {
        return findById(sessionId).orElseThrow(NoSuchSessionException::new);
    }

    default AppleGame getBy(Long ownerId, Long sessionId) {
        return findByOwnerIdAndSessionId(ownerId, sessionId)
                .orElseThrow(NoSuchSessionException::new);
    }

    Optional<AppleGame> findByOwnerIdAndSessionId(Long ownerId, Long sessionId);
}
