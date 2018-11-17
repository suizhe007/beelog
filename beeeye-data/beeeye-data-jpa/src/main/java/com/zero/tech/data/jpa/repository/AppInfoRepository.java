package com.zero.tech.data.jpa.repository;

import com.zero.tech.data.jpa.domain.AppInfo;
import com.zero.tech.data.jpa.dto.AppStatusDto;
import com.zero.tech.data.jpa.pk.AppInfoPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1
 * @desc
 * @date 2016-10-09 09:57:28
 * AppInfo是要操作的实体类，AppInfoPK是实体类主键的类型
 */
public interface AppInfoRepository extends CrudRepository<AppInfo, AppInfoPK> {

    /**
     * 根据sql查询
     * 使用@Query注解来自定义sql查询
     * @return
     */
    @Query(value = "select new com.zero.tech.data.jpa.dto.AppStatusDto(a.appInfoPK.host, a.appInfoPK.app, a.status, " +
            "a.deploy) from AppInfo a where a.appInfoPK.type=?1")
    List<AppStatusDto> findBySql(int type);

    @Query(value = "select new com.zero.tech.data.jpa.dto.AppStatusDto(a.appInfoPK.host, a.appInfoPK.app, a.status, " +
            "a.deploy) from AppInfo a where a.appInfoPK.host=?1 and a.appInfoPK.app like ?2 and a.appInfoPK.type=?3")
    List<AppStatusDto> findBySql(String host, String app, int type);

    @Query(value = "select new com.zero.tech.data.jpa.dto.AppStatusDto(a.appInfoPK.host, a.appInfoPK.app, a.status, " +
            "a.deploy) from AppInfo a where a.appInfoPK.host=?1 and a.appInfoPK.type=?2")
    List<AppStatusDto> findBySql(String host, int type);

    @Query(value = "select new com.zero.tech.data.jpa.dto.AppStatusDto(a.appInfoPK.host, a.appInfoPK.app, a.status, " +
            "a.deploy) from AppInfo a where a.appInfoPK.app like ?1 and a.appInfoPK.type=?2")
    List<AppStatusDto> findBySqlApp(String app, int type);
}