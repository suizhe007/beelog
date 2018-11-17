package com.zero.tech.alarm.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class AbstractQuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        try {
            if (QuartzTaskManager.registerRunningJob(this)) {
                doExecution(jobExecutionContext);
            }
        } catch (Exception ex) {

        } finally {
            QuartzTaskManager.removeRunningJob(this);
        }
    }

    protected abstract void doExecution(JobExecutionContext jobExecutionContext);
}
