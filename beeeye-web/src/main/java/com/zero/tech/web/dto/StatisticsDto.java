package com.zero.tech.web.dto;

/**
 * @desc 实时统计的数据返回的
 */
public class StatisticsDto {

    private String name;

    private String time;

    private int succ;

    private int fail;

    public StatisticsDto() {
    }

    public StatisticsDto(String name, String time, int succ, int fail) {
        this.name = name;
        this.time = time;
        this.succ = succ;
        this.fail = fail;
    }

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
