package com.zero.tech.data.db.dto;

/**
 * @desc name info dto
 */
public class NameInfoDto {

    private String name;
    private String type;
    private String app;

    public NameInfoDto() {
    }

    public NameInfoDto(String name, String type, String app) {
        this.name = name;
        this.type = type;
        this.app = app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
