package com.zero.tech.alarm.quartz;

import com.zero.tech.alarm.configuration.CommonProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AlarmJobConfig
        implements InitializingBean {
    @Autowired
    CommonProperties commonProperties;
    @Autowired
    private QuartzTaskManager quartzTaskManager;

    @Override
    public void afterPropertiesSet()
            throws Exception {
        if (!quartzTaskManager.isPresentTrigger(commonProperties.getAlarmGroupName(), commonProperties.getAlarmTriggerName())) {
            quartzTaskManager.addJob(commonProperties.getAlarmJobName(), commonProperties.getAlarmTriggerName(),
                    commonProperties.getAlarmGroupName(), AlarmJob.class, commonProperties.getAlarmCron(), new HashMap<>());
        }
    }
}
