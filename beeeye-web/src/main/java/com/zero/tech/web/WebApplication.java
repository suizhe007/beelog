package com.zero.tech.web;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import static com.zero.tech.base.util.Utils.checkEnv;

/**
 * @desc web项目启动
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.zero.tech.web",
        "com.zero.tech.data.db", "com.zero.tech.data.redis"})
@MapperScan(basePackages = {"com.zero.tech.data.db.*"})
public class WebApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(WebApplication.class);
        builder.run(args);
        checkEnv(LOGGER);
        LOGGER.info("web start successfully");
    }
}
