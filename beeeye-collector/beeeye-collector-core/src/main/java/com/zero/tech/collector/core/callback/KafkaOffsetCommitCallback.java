package com.zero.tech.collector.core.callback;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaOffsetCommitCallback implements OffsetCommitCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOffsetCommitCallback.class);

    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        if (null != exception) {
            // 如果异步提交发生了异常
            LOGGER.error("commit failed for offsets {}, and exception is {}", offsets, exception);
        }
    }
}
