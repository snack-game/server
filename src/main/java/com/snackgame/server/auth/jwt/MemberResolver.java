package com.snackgame.server.auth.jwt;

import java.util.Optional;

public interface MemberResolver<T> {

    Optional<T> resolve(Long memberId);
}
