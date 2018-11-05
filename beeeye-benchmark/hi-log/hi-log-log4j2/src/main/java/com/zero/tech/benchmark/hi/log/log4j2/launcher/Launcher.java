package com.zero.tech.benchmark.hi.log.log4j2.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;

import java.util.Iterator;
import java.util.Set;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc
 * @date 2017-08-13 16:52:22
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Launcher {

    private static volatile boolean RUNNING = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    /**
     * slf4j与log4j2集成：
     * 需要的jar包：
     * slf4j-api
     * log4j-api
     * log4j-core
     * log4j-slf4j-impl(集成包)
     * <p>
     * <p>
     * 由于SpringApplication使用commons-logging编写log,所以需要集成commons-logging,转换为slf4j输出
     * pom文件中解除jcl-over-slf4j
     * commons-logging与slf4j集成：
     * 需要的jar包：
     * jcl-over-slf4j(集成包，不再需要commons-logging)
     * slf4j-api
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Launcher.class);
        Set<ApplicationListener<?>> listeners = builder.application().getListeners();
        for (Iterator<ApplicationListener<?>> it = listeners.iterator(); it.hasNext();) {
            ApplicationListener<?> listener = it.next();
            if (listener instanceof LoggingApplicationListener) {
                it.remove();
            }
        }
        builder.application().setListeners(listeners);
        builder.run(args);

        LOGGER.info("hi-log-log4j2 start successfully");

        String a = "哈哈";
        while (true) {
            LOGGER.info("i am test, {}", a);
            Thread.sleep(5000);
        }
    }
}
