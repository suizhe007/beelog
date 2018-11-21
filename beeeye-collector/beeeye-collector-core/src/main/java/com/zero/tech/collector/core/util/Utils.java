package com.zero.tech.collector.core.util;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;

import java.util.Collection;

public class Utils {
    public static void assignedPartitionOffsets(Logger LOGGER, KafkaConsumer kafkaConsumer, Collection<TopicPartition> partitions) {
        LOGGER.info("after rebalance, reset offset once");
        for (TopicPartition partition : partitions) {
            //获取消费偏移量，实现原理是向协调者发送获取请求
            OffsetAndMetadata offset = kafkaConsumer.committed(partition);
            //设置本地拉取偏移量，下次拉取消息以这个偏移量为准
            if (offset != null) {
                kafkaConsumer.seek(partition, offset.offset());
                LOGGER.info("reset offset {}-{}", partition.partition(), offset.offset());
            } else {
                LOGGER.info("current offset {}-0", partition.partition());
            }
        }
    }
}
