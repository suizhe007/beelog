package com.zero.tech.monitor.service;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.constant.LogCollectionStatus;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @desc 缓存service，缓存当前服务器中所有的上线的应用和历史上线应用，包含本进程缓存和第三方redis缓存（提供给web进行展示使用）
 */
@Service
public class CacheService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    public static List<String> appHosts = new ArrayList<String>();

    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private AppStatusMonitorService appStatusMonitorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("开始加载缓存");
        // 将mysql数据进行清空
        this.appInfoService.deleteAll();
        if (Objects.isNull(curatorFramework.checkExists().forPath(Constants.ROOT_PATH_EPHEMERAL))) {
            curatorFramework.create().creatingParentsIfNeeded().forPath(Constants.ROOT_PATH_EPHEMERAL);
        }
        List<String> apps = curatorFramework.getChildren().forPath(Constants.ROOT_PATH_EPHEMERAL);

        // 启动时获取所有的节点数据, 写入本地缓存和mysql
        for (String app : apps) {
            List<String> hosts = curatorFramework.getChildren().forPath(Constants.ROOT_PATH_EPHEMERAL + Constants.SLASH + app);
            for (String host : hosts) {
                appHosts.add(Constants.ROOT_PATH_EPHEMERAL + Constants.SLASH + app + Constants.SLASH + host);
                this.appInfoService.add(host, app, Constants.ZK_NODE_TYPE_EPHEMERAL, this.calLogCollectionStatus(app, host));
            }
        }

        if (Objects.isNull(curatorFramework.checkExists().forPath(Constants.ROOT_PATH_PERSISTENT))) {
            curatorFramework.create().creatingParentsIfNeeded().forPath(Constants.ROOT_PATH_PERSISTENT);
        }
        apps = curatorFramework.getChildren().forPath(Constants.ROOT_PATH_PERSISTENT);
        for (String app : apps) {
            List<String> hosts = curatorFramework.getChildren().forPath(Constants.ROOT_PATH_PERSISTENT + Constants.SLASH + app);
            for (String host : hosts) {
                this.appInfoService.add(host, app, Constants.ZK_NODE_TYPE_PERSISTENT, LogCollectionStatus.HISTORY);
            }
        }

        LOGGER.info("加载缓存结束");

        this.appStatusMonitorService.init();
    }

    /**
     * 根据app和host计算LogCollectionStatus
     *
     * @param app
     * @param host
     * @return
     */
    private LogCollectionStatus calLogCollectionStatus(String app, String host) {
        String[] datas = this.zkClient.readData(Constants.ROOT_PATH_EPHEMERAL + Constants.SLASH + app + Constants.SLASH + host).toString().split(Constants.SEMICOLON);
        if (datas[0].equals(Constants.APPENDER_INIT_DATA) || datas[0].equals(Constants.APP_APPENDER_RESTART_KEY)) {
            return LogCollectionStatus.RUNNING;
        }
        return LogCollectionStatus.STOPPED;
    }
}
