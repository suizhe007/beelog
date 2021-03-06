package com.zero.tech.collector.metrics.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @desc zk的配置
 */
@Configuration
@ConfigurationProperties(prefix = "coordinate.zookeeper")
public class ZookeeperConfiguration {

    private String zkServers;

    private int sessionTimeout;

    private int connectionTimeout;

    @Bean
    public ZkClient zkClient() {
        return new ZkClient(this.zkServers, this.sessionTimeout, this.connectionTimeout);
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
