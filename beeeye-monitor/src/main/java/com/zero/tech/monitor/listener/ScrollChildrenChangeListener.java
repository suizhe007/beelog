package com.zero.tech.monitor.listener;

import com.zero.tech.data.redis.service.RedisService;
import com.zero.tech.monitor.service.AppInfoService;
import io.lettuce.core.models.role.RedisSentinelInstance;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc app的root节点变化监听
 * @date 2016-09-23 14:49:36
 */
public class ScrollChildrenChangeListener implements PathChildrenCacheListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrollChildrenChangeListener.class);

    private RedisService rabbitmqService;

    private ZkClient zkClient;

    private AppInfoService appInfoService;

    public ScrollChildrenChangeListener(RedisService redisService, ZkClient zkClient, AppInfoService appInfoService) {
        this.rabbitmqService = rabbitmqService;
        this.zkClient = zkClient;
        this.appInfoService = appInfoService;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        switch (event.getType()) {
            case CHILD_ADDED:
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, event.getData().getPath(), true);
                pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                pathChildrenCache.getListenable().addListener(new AppChildrenChangeListener(this.rabbitmqService, this.zkClient, this.appInfoService));
                LOGGER.info("app added: " + event.getData().getPath());
                break;
            case CHILD_REMOVED:
                LOGGER.info("app removed: " + event.getData().getPath());
                break;
        }
    }
}
