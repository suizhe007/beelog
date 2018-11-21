package com.zero.tech.collector.core.configuration.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @desc kafka配置项
 */
@ConfigurationProperties(prefix = "message.kafka")
public class KafkaProperties {

    private String brokers;

    private String consumeGroup;

    private long pollTimeout;

    private String topic;

    public String getBrokers() {
        return brokers;
    }

    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }

    public String getConsumeGroup() {
        return consumeGroup;
    }

    public void setConsumeGroup(String consumeGroup) {
        this.consumeGroup = consumeGroup;
    }

    public long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
