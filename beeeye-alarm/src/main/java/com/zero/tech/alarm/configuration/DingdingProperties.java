package com.zero.tech.alarm.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alert.dingding")
public class DingdingProperties {

    private String url;

    private boolean switchFlag;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSwitchFlag() {
        return switchFlag;
    }

    public void setSwitchFlag(boolean switchFlag) {
        this.switchFlag = switchFlag;
    }
}
