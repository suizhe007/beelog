package com.zero.tech.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.monitor", "com.zero.tech.data.db", "com.zero.tech.data.redis"})
public class MonitorApplication {

    private static volatile boolean RUNNING = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MonitorApplication.class);
        builder.run(args);
        LOGGER.info("monitor start successfully");

        // 优雅停止项目
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("try to stop the system");
                synchronized (MonitorApplication.class) {
                    RUNNING = false;
                    MonitorApplication.class.notify();
                }
            }
        });

        synchronized (MonitorApplication.class) {
            while (RUNNING) {
                try {
                    MonitorApplication.class.wait();
                } catch (InterruptedException e) {
                    LOGGER.error("wait error");
                    e.printStackTrace();
                }
            }
        }
    }
}
