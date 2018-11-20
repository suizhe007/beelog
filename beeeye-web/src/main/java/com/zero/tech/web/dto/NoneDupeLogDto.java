package com.zero.tech.web.dto;

/**
 */
public class NoneDupeLogDto {

    private String flag;

    private String log;

    public NoneDupeLogDto() {
    }

    public NoneDupeLogDto(String flag, String log) {
        this.flag = flag;
        this.log = log;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
