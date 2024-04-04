package com.snackgame.server.applegame.domain.game;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snackgame.server.applegame.exception.NoSuchSessionException;
import com.snackgame.server.applegame.exception.SessionNotFinishedException;

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

    @Query(value = "with scores as ("
                   + "select percent_rank() over (order by score desc) as percentile, session_id, score "
                   + "from apple_game where finished_at is not null "
                   + ") "
                   + "select percentile "
                   + "    from scores where session_id = :sessionId", nativeQuery = true)
    Optional<Double> findPercentileOf(long sessionId);

    default Percentile ratePercentileOf(long sessionId) {
        return findPercentileOf(sessionId)
                .map(Percentile::new)
                .orElseThrow(SessionNotFinishedException::new);
    }

    Optional<AppleGame> findByOwnerIdAndSessionId(Long ownerId, Long sessionId);
}
