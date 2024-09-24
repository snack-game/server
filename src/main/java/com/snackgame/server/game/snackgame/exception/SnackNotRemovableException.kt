package com.snackgame.server.game.snackgame.exception

import com.snackgame.server.common.exception.Kind

class SnackNotRemovableException(message: String, kind: Kind = Kind.BAD_REQUEST) : SnackgameException(message, kind)
