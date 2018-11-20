package com.zero.tech.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import static com.zero.tech.base.util.Utils.checkEnv;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.alarm", "com.zero.tech.data.redis"})
public class AlarmApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmApplication.class);
    private static volatile boolean RUNNING = true;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlarmApplication.class);
        builder.run(args);
        checkEnv(LOGGER);
        LOGGER.info("alarm start successfully");

        // 优雅停止项目
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("try to stop the system");
                synchronized (AlarmApplication.class) {
                    RUNNING = false;
                    AlarmApplication.class.notify();
                }
            }
        });

        synchronized (AlarmApplication.class) {
            while (RUNNING) {
                try {
                    AlarmApplication.class.wait();
                } catch (InterruptedException e) {
                    LOGGER.error("wait error", e);
                }
            }
        }
    }
}
