package com.zero.tech.collector.metrics.task.job;

import com.zero.tech.base.constant.EventType;
import com.zero.tech.base.dto.EventLog;
import com.zero.tech.base.dto.LogDto;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import java.util.List;

/**
 * @desc info collect consume group的具体业务处理根类
 */
public abstract class Job {

    // 下一个处理链条
    private Job nextJob;
    // 每个job能处理的消息类型
    private List<EventType> types;

    /**
     * 执行具体的业务处理
     */
    public abstract void doJob(EventLog log, LogDto logDto, BulkRequestBuilder bulkRequest);

    public Job(List<EventType> types) {
        this.types = types;
    }

    public Job getNextJob() {
        return nextJob;
    }

    public void setNextJob(Job nextJob) {
        this.nextJob = nextJob;
    }

    public List<EventType> getTypes() {
        return types;
    }

    public void setTypes(List<EventType> types) {
        this.types = types;
    }
}
