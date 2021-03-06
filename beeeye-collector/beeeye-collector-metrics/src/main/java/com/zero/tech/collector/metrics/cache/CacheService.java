package com.zero.tech.collector.metrics.cache;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.constant.NameInfoType;
import com.zero.tech.data.db.domain.NameInfo;
import com.zero.tech.data.db.mapper.NameInfoMapper;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @desc 缓存名称采集相关的数据
 */
@Service
public class CacheService implements InitializingBean {

    private static Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private NameInfoMapper nameInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    private SetOperations<String, String> setOps;

    private static final String CONFIG_PREFIX = "jthink_monitor_collector";
    private static final String API_NAME_PREFIX = "jthink_monitor_collector_api_name";
    private static final String ACCOUNT_NAME_PREFIX = "jthink_monitor_collector_account_name";
    private static final String THIRD_NAME_PREFIX = "jthink_monitor_collector_third_name";
    private static final String MIDDLEWARE_NAME_PREFIX = "jthink_monitor_collector_middleware_name";

    private static final Map<String, String> mapping = new HashMap<String, String>() {
        {
            put(NameInfoType.API.symbol(), API_NAME_PREFIX);
            put(NameInfoType.ACCOUNT.symbol(), ACCOUNT_NAME_PREFIX);
            put(NameInfoType.THIRD.symbol(), THIRD_NAME_PREFIX);
            put(NameInfoType.MIDDLEWARE.symbol(), MIDDLEWARE_NAME_PREFIX);
        }
    };

    /**
     * 保存
     *
     * @param nameInfo
     */
    public void save(NameInfo nameInfo) {
        nameInfoMapper.save(nameInfo);
    }

    /**
     * 根据采集的类型和值存入redis
     *
     * @param type
     * @param value
     * @return
     */
    public void add(String type, String value) {
        this.setOps.add(mapping.get(type), value);
    }

    /**
     * 根据采集的类型和值判断是否存在
     *
     * @param type
     * @param value
     * @return
     */
    public boolean isExists(String type, String value) {
        return this.setOps.isMember(mapping.get(type), value);
    }

    /**
     * 将数据库中的配置表进行缓存
     */
    private void loadCache() {
        StopWatch sw = new StopWatch();
        sw.start();
        LOGGER.info("start load config to cache");

        Iterator<NameInfo> it = nameInfoMapper.findAll().iterator();

        for (; it.hasNext(); ) {
            NameInfo nameInfo = it.next();
            this.setOps.add(mapping.get(nameInfo.getType()), nameInfo.getName());
        }

        sw.stop();
        LOGGER.info("load config to cache end, cost {} ms", sw.getTime());
    }

    /**
     * 将redis中的配置信息清除
     */
    private void clearCache() {
        StopWatch sw = new StopWatch();
        sw.start();
        LOGGER.info("start clear config cache");

        Set<String> keys = this.redisTemplate.keys(CONFIG_PREFIX + Constants.XING_HAO);
        this.redisTemplate.delete(keys);

        sw.stop();
        LOGGER.info("clear config cache end, cost {} ms", sw.getTime());
    }

    /**
     * 缓存数据初始化
     */
    public void load() {
        this.clearCache();
        this.loadCache();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setOps = this.redisTemplate.opsForSet();
        this.load();
    }
}
