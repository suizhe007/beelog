package com.zero.tech.alarm.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties {
    private String alarmCron;
    private String alarmJobName;
    private String alarmGroupName;
    private String alarmTriggerName;

    public String getAlarmCron() {
        return alarmCron;
    }

    public String getAlarmJobName() {
        return alarmJobName;
    }

    public void setAlarmJobName(String alarmJobName) {
        this.alarmJobName = alarmJobName;
    }

    public String getAlarmGroupName() {
        return alarmGroupName;
    }

    public void setAlarmGroupName(String alarmGroupName) {
        this.alarmGroupName = alarmGroupName;
    }

    public String getAlarmTriggerName() {
        return alarmTriggerName;
    }

    public void setAlarmTriggerName(String alarmTriggerName) {
        this.alarmTriggerName = alarmTriggerName;
    }

    public void setAlarmCron(String alarmCron) {
        this.alarmCron = alarmCron;
    }
}
