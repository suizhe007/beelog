package com.zero.tech.collector.indexer;

import com.zero.tech.collector.core.hook.ShutdownHookRunner;
import com.zero.tech.collector.core.task.Task;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static com.zero.tech.base.util.Utils.checkEnv;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.collector.core", "com.zero.tech.collector.indexer"})
public class IndexerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexerApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(IndexerApplication.class);
        ConfigurableApplicationContext context = builder.run(args);
        checkEnv(LOGGER);
        LOGGER.info("collector indexer start successfully");

        KafkaConsumer kafkaConsumer = (KafkaConsumer<byte[], String>) context.getBean("kafkaConsumer");
        Task task = (Task) context.getBean("indexerTask");

        // 优雅停止项目
        Runtime.getRuntime().addShutdownHook(new ShutdownHookRunner(kafkaConsumer, task));
        task.doTask();
    }
}
