package com.zero.tech.collector.indexer.balancer;

import com.zero.tech.collector.core.configuration.kafka.KafkaProperties;
import com.zero.tech.collector.indexer.task.IndexerTask;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * @desc rebalance回调
 */
@Component
public class HandleRebalance implements ConsumerRebalanceListener, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandleRebalance.class);

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        this.kafkaConsumer.commitSync(IndexerTask.currentOffsets);
        LOGGER.info("before rebalance, commit offset once");
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        this.kafkaConsumer.subscribe(Arrays.asList(this.kafkaProperties.getTopic()), this);
    }
}