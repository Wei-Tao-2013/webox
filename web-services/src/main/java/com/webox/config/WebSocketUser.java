package com.webox.config;

import java.security.Principal;

public final class WebSocketUser implements Principal {

    private final String name;

    public WebSocketUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}