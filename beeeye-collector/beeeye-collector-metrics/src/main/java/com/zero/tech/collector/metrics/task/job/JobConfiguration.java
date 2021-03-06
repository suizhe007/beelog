package com.zero.tech.collector.metrics.task.job;

import com.zero.tech.base.constant.EventType;
import com.zero.tech.collector.core.configuration.es.EsProperties;
import com.zero.tech.collector.metrics.cache.CacheService;
import com.zero.tech.data.redis.service.RedisService;
import org.I0Itec.zkclient.ZkClient;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @desc 责任链组装3个job
 */
@Configuration
public class JobConfiguration {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private EsProperties esProperties;
    @Autowired
    private TransportClient transportClient;

    // 以下3个bean进行责任链的生成和组装
    @Bean
    public ExceptionProcessor exceptionProcessor() {
        List<EventType> exceptionProcesses = Arrays.asList(EventType.job_execute, EventType.thirdparty_call, EventType.middleware_opt, EventType.invoke_interface);
        ExceptionProcessor exceptionProcessor = new ExceptionProcessor(exceptionProcesses);
        exceptionProcessor.setRedisService(this.redisService);
        exceptionProcessor.setZkClient(this.zkClient);
        return exceptionProcessor;
    }

    @Bean
    public NameCollector nameCollector(ExceptionProcessor exceptionProcessor) {
        List<EventType> names = Arrays.asList(EventType.invoke_interface, EventType.thirdparty_call, EventType.middleware_opt);
        NameCollector nameCollector = new NameCollector(names);
        nameCollector.setNextJob(exceptionProcessor);
        nameCollector.setCacheService(this.cacheService);
        return nameCollector;
    }

    @Bean
    public Indexer indexer(NameCollector nameCollector) {
        List<EventType> indexes = Arrays.asList(EventType.job_execute, EventType.thirdparty_call, EventType.middleware_opt, EventType.invoke_interface);
        Indexer indexer = new Indexer(indexes);
        indexer.setNextJob(nameCollector);
        indexer.setEsProperties(this.esProperties);
        indexer.setTransportClient(this.transportClient);
        return indexer;
    }
}
