package com.snackgame.server.auth.token.support

interface GuestResolver<T> {

    fun resolve(guestId: Long): T?
}
