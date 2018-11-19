package com.zero.tech.data.db.domain;

/**
 * @desc name info entity
 */
public class NameInfo {
    private String name;
    private String type;
    // 来自哪个app
    private String app;
    // 属于哪个报警模板
    private Integer tid;

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

    public Integer getTid() {
        return tid;
    }

    public NameInfo setTid(Integer tid) {
        this.tid = tid;
        return this;
    }
}
