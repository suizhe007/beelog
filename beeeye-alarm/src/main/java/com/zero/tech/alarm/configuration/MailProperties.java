package com.zero.tech.alarm.configuration;

import com.zero.tech.base.constant.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String from;
    private String cc;
    private boolean switchFlag;

    public List<String> getCcs() {
        return Arrays.asList(cc.split(Constants.COMMA));
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public boolean isSwitchFlag() {
        return switchFlag;
    }

    public MailProperties setSwitchFlag(boolean switchFlag) {
        this.switchFlag = switchFlag;
        return this;
    }
}
