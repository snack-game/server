package com.snackgame.server.auth.oauth.oidc.payload

interface IdTokenPayload {
    val provider: String
    val id: String
    val email: String
    val name: String?
    val picture: String?
}
