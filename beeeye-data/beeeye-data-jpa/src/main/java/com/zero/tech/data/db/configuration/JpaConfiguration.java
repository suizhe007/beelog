package com.zero.tech.data.db.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @desc jpa相关的配置
 */
@Configuration
@EnableTransactionManagement
@EntityScan("com.zero.tech.data.jpa.domain")
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager", basePackages = {"com.zero.tech.data.db.mapper"})
public class JpaConfiguration {
}
