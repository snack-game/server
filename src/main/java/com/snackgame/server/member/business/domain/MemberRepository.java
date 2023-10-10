package com.snackgame.server.member.business.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackgame.server.member.business.exception.MemberNotFoundException;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    Optional<Member> findByName(Name name);

    default Member getByName(Name name) {
        return findByName(name)
                .orElseThrow(MemberNotFoundException::new);
    }

    boolean existsByName(Name name);

    List<Member> findByNameStringStartingWith(String prefix);
}
