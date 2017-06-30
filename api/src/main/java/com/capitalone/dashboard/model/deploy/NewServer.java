package com.capitalone.dashboard.model.deploy;

import com.capitalone.dashboard.model.Host;

public class NewServer extends Host{
    private final String name;
    private final boolean online;

    public NewServer(String name, boolean online) {
        this.name = name;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }
}
