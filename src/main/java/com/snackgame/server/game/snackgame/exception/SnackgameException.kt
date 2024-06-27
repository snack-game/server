package com.snackgame.server.game.snackgame.exception

import com.snackgame.server.common.exception.BusinessException
import com.snackgame.server.common.exception.Kind

abstract class SnackgameException(message: String, kind: Kind = Kind.BAD_REQUEST) : BusinessException(kind, message)
