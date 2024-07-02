package com.snackgame.server.rank.exception

import com.snackgame.server.common.exception.BusinessException
import com.snackgame.server.common.exception.Kind

abstract class RankException(message: String, kind: Kind = Kind.BAD_REQUEST) : BusinessException(kind, message)
