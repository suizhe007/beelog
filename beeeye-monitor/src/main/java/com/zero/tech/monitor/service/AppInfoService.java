package com.zero.tech.monitor.service;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.constant.LogCollectionStatus;
import com.zero.tech.data.db.domain.AppInfo;
import com.zero.tech.data.db.mapper.AppInfoMapper;
import com.zero.tech.monitor.listener.AppChildrenChangeListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppChildrenChangeListener.class);

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private AppInfoMapper appInfoMapper;

    /**
     * 保存appInfo
     *
     * @param host
     * @param app
     * @param type
     * @param logCollectionStatus
     */
    public void add(String host, String app, int type, LogCollectionStatus logCollectionStatus) {
        AppInfo appInfo = new AppInfo();
        appInfo.setHost(host);
        appInfo.setApp(app);
        appInfo.setType(type);
        appInfo.setStatus(logCollectionStatus.symbol());
        if (logCollectionStatus.symbol().equals(LogCollectionStatus.HISTORY.symbol())) {
            appInfo.setDeploy(this.getDeploy(Constants.ROOT_PATH_PERSISTENT + Constants.SLASH + app + Constants.SLASH + host));
        } else {
            appInfo.setDeploy(this.getDeploy(Constants.ROOT_PATH_EPHEMERAL + Constants.SLASH + app + Constants.SLASH + host));
        }
        appInfoMapper.save(appInfo);
    }

    /**
     * 修改记录的收集日志状态
     *
     * @param host
     * @param app
     * @param type
     * @param logCollectionStatus
     */
    public void update(String host, String app, int type, LogCollectionStatus logCollectionStatus) {
        AppInfo appInfo = appInfoMapper.findAppInfoByPK(host, app, type).get(0);
        appInfo.setStatus(logCollectionStatus.symbol());
        appInfoMapper.save(appInfo);
    }

    /**
     * 根据host和app进行删除
     *
     * @param host
     * @param app
     * @param type
     */
    public void delete(String host, String app, int type) {
        List<AppInfo> appInfos = appInfoMapper.findAppInfoByPK(host, app, type);
        if (appInfos != null) {
            if (appInfos.size() == 1) {
                AppInfo appInfo = appInfoMapper.findAppInfoByPK(host, app, type).get(0);
                appInfoMapper.delete(appInfo);
            } else if (appInfos.size() > 1) {
                LOGGER.error("host {},app {},type {} exist repeatedly");
            } else {
                LOGGER.error("host {},app {},type {} does not exist");
            }
        }
    }

    /**
     * 删除所有的数据
     */
    public void deleteAll() {
        appInfoMapper.deleteAll();
    }

    /**
     * 获取app的部署位置
     *
     * @param path
     * @return
     */
    private String getDeploy(String path) {
        String[] datas = this.zkClient.readData(path).toString().split(Constants.SEMICOLON);
        return datas[datas.length - 1];
    }
}
