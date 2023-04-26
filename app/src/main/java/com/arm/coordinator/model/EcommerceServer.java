package com.arm.coordinator.model;

public class EcommerceServer {

    private final String hostname;
    private final int port;

    public EcommerceServer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getServerName() {
        return hostname + ":" + port;
    }

}
