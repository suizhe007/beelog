package com.zero.tech.data.db.mapper;

import com.zero.tech.data.db.domain.AppInfo;
import com.zero.tech.data.db.dto.AppStatusDto;

import java.util.List;

public interface AppInfoMapper {
    List<AppStatusDto> findByType(int type);

    List<AppStatusDto> findAppStatusByPK(String host, String app, int type);

    AppInfo findAppInfoByPK(String host, String app, int type);

    List<AppStatusDto> findByHostType(String host, int type);

    List<AppStatusDto> findByAppType(String app, int type);

    void save(AppInfo appInfo);

    void deleteAll();

    void delete(AppInfo appInfo);
}
