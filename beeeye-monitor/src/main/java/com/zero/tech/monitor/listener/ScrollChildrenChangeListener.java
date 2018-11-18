package com.zero.tech.monitor.listener;

import com.zero.tech.data.redis.service.RedisService;
import com.zero.tech.monitor.service.AppInfoService;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc app的root节点变化监听
 */
public class ScrollChildrenChangeListener implements PathChildrenCacheListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrollChildrenChangeListener.class);

    private RedisService redisService;

    private ZkClient zkClient;

    private AppInfoService appInfoService;

    public ScrollChildrenChangeListener(RedisService redisService, ZkClient zkClient, AppInfoService appInfoService) {
        this.redisService = redisService;
        this.zkClient = zkClient;
        this.appInfoService = appInfoService;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        switch (event.getType()) {
            case CHILD_ADDED:
                PathChildrenCache pathChildrenCache = new PathChildrenCache(client, event.getData().getPath(), true);
                pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                pathChildrenCache.getListenable().addListener(new AppChildrenChangeListener(this.redisService, this.zkClient, this.appInfoService));
                LOGGER.info("app added: " + event.getData().getPath());
                break;
            case CHILD_REMOVED:
                LOGGER.info("app removed: " + event.getData().getPath());
                break;
        }
    }
}
