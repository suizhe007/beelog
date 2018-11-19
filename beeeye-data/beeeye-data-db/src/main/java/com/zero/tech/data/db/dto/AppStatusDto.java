package com.zero.tech.data.db.dto;

public class AppStatusDto {

    private String host;

    private String app;

    private String status;

    private String deploy;

    public AppStatusDto(String host, String app, String status, String deploy) {
        this.host = host;
        this.app = app;
        this.status = status;
        this.deploy = deploy;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeploy() {
        return deploy;
    }

    public void setDeploy(String deploy) {
        this.deploy = deploy;
    }
}
