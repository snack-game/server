package com.snackgame.server.game.session.exception

import com.snackgame.server.common.exception.BusinessException
import com.snackgame.server.common.exception.Kind

abstract class SessionException(message: String, kind: Kind = Kind.BAD_REQUEST) : BusinessException(kind, message)
