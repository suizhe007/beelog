package com.zero.tech.collector.metrics.balancer;

import com.zero.tech.collector.core.configuration.kafka.KafkaProperties;
import com.zero.tech.collector.metrics.task.MetricsTask;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

import static com.zero.tech.collector.core.util.Utils.assignedPartitionOffsets;

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
        this.kafkaConsumer.commitSync(MetricsTask.currentOffsets);
        LOGGER.info("before rebalance, commit offset once");
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        assignedPartitionOffsets(LOGGER, kafkaConsumer, partitions);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.kafkaConsumer.subscribe(Arrays.asList(this.kafkaProperties.getTopic()), this);
    }
}