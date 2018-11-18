package com.zero.tech.web.service;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.data.db.dto.AppStatusDto;
import com.zero.tech.data.db.mapper.AppInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @desc app监控的service
 */
@Service
public class AppMonitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMonitorService.class);
    @Autowired
    private AppInfoMapper appInfoMapper;

    /**
     * 根据host、app、type查询状态信息
     *
     * @param host
     * @param app
     * @param type
     * @return
     */
    public List<AppStatusDto> getHostAppInfo(String host, String app, int type) {
        if (null == host) {
            return appInfoMapper.findByType(type);
        } else {
            if (null == app) {
                return appInfoMapper.findByHostType(host, type);
            } else {
                app = app + Constants.PERCENT;
                return this.convert(appInfoMapper.findAppStatusByPK(host, app, type), app);
            }
        }
    }

    /**
     * 根据host和app type查询状态信息
     *
     * @param host
     * @param app
     * @param type
     * @return
     */
    public List<AppStatusDto> getAppHostInfo(String host, String app, int type) {
        if (null == app) {
            return appInfoMapper.findByType(type);
        } else {
            app = app + Constants.PERCENT;
            if (null == host) {
                return this.convert(appInfoMapper.findByAppType(app, type), app);
            } else {
                return this.convert(appInfoMapper.findAppStatusByPK(host, app, type), app);
            }
        }
    }

    /**
     * 去掉项目名字前缀一样的
     *
     * @param dtos
     * @param app
     * @return
     */
    private List<AppStatusDto> convert(List<AppStatusDto> dtos, String app) {
        app = app.substring(0, app.length() - 1);
        for (int i = 0; i < dtos.size(); ++i) {
            String a = dtos.get(i).getApp();
            if (!a.equals(app) && !a.replace(app, Constants.EMPTY_STR).startsWith(Constants.JING_HAO)) {
                dtos.remove(i--);
            }
        }
        return dtos;
    }

    /**
     * 获得所有的host和app
     *
     * @param type
     * @return
     */
    public Map<String, Set<String>> getHostApp(int type, boolean isDeploy) {
        Map<String, Set<String>> hostApps = new HashMap<>();
        List<AppStatusDto> appStatusDtos = appInfoMapper.findByType(type);
        Set<String> apps = null;
        for (AppStatusDto dto : appStatusDtos) {
            String app = dto.getApp();
            if (app.contains(Constants.JING_HAO) && isDeploy) {
                // 如果是V1.2.0以及以后版本，对同一个app部署多个实例在同一个host上进行编号
                dto.setApp(app.split(Constants.JING_HAO)[0]);
            }
            String host = dto.getHost();
            if (hostApps.containsKey(host)) {
                // 如果已经存在该host
                Set<String> existApps = hostApps.get(host);
                existApps.add(dto.getApp());
            } else {
                // 未存在host
                apps = new HashSet<String>();
                apps.add(dto.getApp());
                hostApps.put(host, apps);
            }
        }
        return hostApps;
    }

    /**
     * 获得所有的host和app
     *
     * @param type
     * @return
     */
    public Map<String, Set<String>> getAppHost(int type) {
        Map<String, Set<String>> appHosts = new HashMap<>();
        List<AppStatusDto> appStatusDtos = appInfoMapper.findByType(type);
        Set<String> hosts = null;
        for (AppStatusDto dto : appStatusDtos) {
            String app = dto.getApp();
            if (app.contains(Constants.JING_HAO)) {
                // 如果是V1.2.0以及以后版本，对同一个app部署多个实例在同一个host上进行编号
                dto.setApp(app.split(Constants.JING_HAO)[0]);
            }
            app = dto.getApp();
            if (appHosts.containsKey(app)) {
                // 如果已经存在该app
                Set<String> existHosts = appHosts.get(app);
                existHosts.add(dto.getHost());
            } else {
                hosts = new HashSet<>();
                hosts.add(dto.getHost());
                appHosts.put(app, hosts);
            }
        }
        return appHosts;
    }
}
