package com.zero.tech.collector.metrics.launcher;

import com.zero.tech.collector.core.hook.ShutdownHookRunner;
import com.zero.tech.collector.core.task.Task;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.util.Iterator;
import java.util.Set;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc 项目启动器
 * @date 2016-08-24 18:31:48
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.collector.core", "com.zero.tech.collector.metrics", "com.zero.tech.data.rabbitmq", "com.zero.tech.data.jpa"})
//@PropertySource("file:/opt/jthink/jthink-config/skyeye/collector/collector-metrics.properties")
@PropertySource("classpath:properties/collector-metrics-dev.properties")
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Launcher.class);
        Set<ApplicationListener<?>> listeners = builder.application().getListeners();
        for (Iterator<ApplicationListener<?>> it = listeners.iterator(); it.hasNext(); ) {
            ApplicationListener<?> listener = it.next();
            if (listener instanceof LoggingApplicationListener) {
                it.remove();
            }
        }
        builder.application().setListeners(listeners);
        ConfigurableApplicationContext context = builder.run(args);
        LOGGER.info("collector metrics start successfully");

        KafkaConsumer kafkaConsumer = (KafkaConsumer<byte[], String>) context.getBean("kafkaConsumer");
        Task task = (Task) context.getBean("metricsTask");

        // 优雅停止项目
        Runtime.getRuntime().addShutdownHook(new ShutdownHookRunner(kafkaConsumer, task));
        task.doTask();
    }

}
