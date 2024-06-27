package com.snackgame.server.auth.oauth.oidc.payload

class GoogleIdTokenPayload(private val payload: Map<String, Any>) : IdTokenPayload {
    override val provider: String
        get() = "google"
    override val id: String
        get() = payload["sub"] as String
    override val email: String
        get() = payload["email"] as String
    override val name: String?
        get() = payload["name"] as String?
    override val picture: String?
        get() = payload["picture"] as String?
}
