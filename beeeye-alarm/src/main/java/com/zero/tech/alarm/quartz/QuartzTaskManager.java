package com.zero.tech.alarm.quartz;

import com.zero.tech.alarm.configuration.CommonProperties;
import org.apache.logging.log4j.util.Strings;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

/*
* Quartz job manager class, we can use this class to remove/add job;
 */
@Component
public class QuartzTaskManager
        implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(QuartzTaskManager.class);
    private static Set<Object> jobs = new HashSet<Object>();
    private static boolean closeRegister = false;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private Scheduler scheduler;

    public static void closeRegister() {
        closeRegister = true;
    }

    public static boolean registerRunningJob(Object job) {
        if (closeRegister) {
            return false;
        }
        jobs.add(job);
        return true;
    }

    public static void removeRunningJob(Object job) {
        jobs.remove(job);
    }

    @Override
    public void afterPropertiesSet()
            throws Exception {
        try {
            scheduler.start();
            scheduler.resumeTrigger(new TriggerKey(commonProperties.getAlarmTriggerName(),
                    commonProperties.getAlarmGroupName()));
        } catch (Exception ex) {
            logger.error("", ex);
            throw ex;
        }
    }

    public boolean removeJobIfCronInequality(String groupName, String jobName, String triggerName, String cron) {
        try {
            Trigger trigger = scheduler.getTrigger(new TriggerKey(triggerName, groupName));
            if (trigger instanceof CronTriggerImpl) {
                CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
                if (!Strings.isBlank(cron) && !cron.equals(cronTrigger.getCronExpression())) {
                    scheduler.deleteJob(new JobKey(jobName, groupName));
                    return true;
                }
            }
        } catch (SchedulerException ex) {
            logger.error("group - " + groupName, ex);
        }
        return false;
    }

    public boolean isPresentTrigger(String group, String triggerName) {
        boolean result = false;
        GroupMatcher groupMatcher = GroupMatcher.groupEquals(group);
        try {
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
            if (triggerKeys != null && triggerKeys.size() > 0) {
                List<Trigger> results = new ArrayList<>(triggerKeys.size());
                for (TriggerKey triggerKey : triggerKeys) {
                    if (triggerKey.getName().equals(triggerName)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (SchedulerException ex) {
            logger.error("group - " + group, ex);
        }
        return result;
    }

    /*
    * Parameter: data will be used in quartz job;
     */
    public boolean addJob(String jobName, String triggerName, String group, Class jobClz, String cron, Map<String, String> data) {
        try {
            if (scheduler.checkExists(new JobKey(jobName, group)) || scheduler.checkExists(new TriggerKey(triggerName, group))) {
                //in quartz, one job can be referred by multiple trigger. here for simplify,Make sure job vs trigger = 1:1
                //这里保证job和trigger是1:1关系
                return false;
            }
            JobDetail jobDetail = JobBuilder
                    .newJob(jobClz).storeDurably(false)
                    .withIdentity(jobName, group)
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, group)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            jobDetail.getJobDataMap().putAll(data);
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (Exception ex) {
            logger.error(MessageFormat.format("add job error,jobName:{0} -triggerName:{1} -group:{2} -data:{3},", jobName, triggerName, group, data), ex);
        }
        return false;
    }
}
