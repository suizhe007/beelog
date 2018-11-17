package com.zero.tech.alarm.configuration;

import com.zero.tech.base.constant.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String smtphost;

    private String port;

    private String from;

    private String cc;

    private String password;

    private boolean switchFlag;

    public List<String> getCcs() {
        return Arrays.asList(cc.split(Constants.COMMA));
    }

    public String getSmtphost() {
        return smtphost;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSwitchFlag() {
        return switchFlag;
    }

    public MailProperties setSwitchFlag(boolean switchFlag) {
        this.switchFlag = switchFlag;
        return this;
    }
}
