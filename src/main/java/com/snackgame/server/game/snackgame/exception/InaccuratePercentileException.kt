package com.snackgame.server.game.snackgame.exception

import com.snackgame.server.common.exception.Kind

class InaccuratePercentileException(percentile: Double) : SnackgameException(
    "백분위 계산이 잘못되었습니다: $percentile", Kind.INTERNAL_SERVER_ERROR
)
