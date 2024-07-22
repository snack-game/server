package com.snackgame.server.auth.exception

class RefreshTokenExpiredException : AuthException(Action.NONE, "리프레시 토큰도 만료되었습니다")
