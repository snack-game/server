package com.snackgame.server.applegame.business.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snackgame.server.applegame.business.exception.NoSuchSessionException;
import com.snackgame.server.member.business.domain.Member;

@Repository
public interface AppleGameSessionRepository extends JpaRepository<AppleGame, Long> {

    @Modifying
    @Query("update AppleGame set owner = :to where owner = :from")
    void transferAll(Member from, Member to);

    @Cacheable(value = "games", key = "#sessionId", unless = "!#result.done")
    default AppleGame getBy(Long sessionId) {
        return findById(sessionId).orElseThrow(NoSuchSessionException::new);
    }
}
