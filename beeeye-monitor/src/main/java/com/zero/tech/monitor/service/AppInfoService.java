package com.zero.tech.monitor.service;

import com.zero.tech.base.constant.Constants;
import com.zero.tech.base.constant.LogCollectionStatus;
import com.zero.tech.data.db.domain.AppInfo;
import com.zero.tech.data.db.mapper.AppInfoMapper;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppInfoService {

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
        AppInfo appInfo = appInfoMapper.findAppInfoByPK(host, app, type).get(0);
        if (null != appInfo) {
            appInfoMapper.delete(appInfo);
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
