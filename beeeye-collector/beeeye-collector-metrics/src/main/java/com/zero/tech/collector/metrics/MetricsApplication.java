package com.zero.tech.collector.metrics;

import com.zero.tech.collector.core.hook.ShutdownHookRunner;
import com.zero.tech.collector.core.task.Task;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static com.zero.tech.base.util.Utils.checkEnv;

/**
 * @desc 项目启动器
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.collector.core", "com.zero.tech.collector.metrics",
        "com.zero.tech.data.redis", "com.zero.tech.data.db"})
@MapperScan(basePackages = {"com.zero.tech.data.db.*"})
public class MetricsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MetricsApplication.class);
        ConfigurableApplicationContext context = builder.run(args);
        checkEnv(LOGGER);
        LOGGER.info("collector metrics start successfully");

        KafkaConsumer kafkaConsumer = (KafkaConsumer<byte[], String>) context.getBean("kafkaConsumer");
        Task task = (Task) context.getBean("metricsTask");
        // 优雅停止项目
        Runtime.getRuntime().addShutdownHook(new ShutdownHookRunner(kafkaConsumer, task));
        task.doTask();
    }
}
