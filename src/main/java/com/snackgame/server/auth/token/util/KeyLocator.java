package com.snackgame.server.auth.token.util;

import java.security.Key;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.LocatorAdapter;

public class KeyLocator extends LocatorAdapter<Key> {

    private final Key key;

    public KeyLocator(Key key) {
        this.key = key;
    }

    @Override
    protected Key locate(JwsHeader header) {
        return key;
    }

    public Key getKey() {
        return key;
    }
}
