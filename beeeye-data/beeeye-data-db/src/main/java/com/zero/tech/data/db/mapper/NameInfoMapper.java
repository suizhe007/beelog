package com.zero.tech.data.db.mapper;

import com.zero.tech.data.db.domain.NameInfo;
import com.zero.tech.data.db.dto.NameInfoDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NameInfoMapper {

    List<NameInfoDto> findByType(String type);

    List<NameInfoDto> findByPK(String type, String name);

    /**
     * 查询属于tid模板的所有name info
     *
     * @param tid
     * @return
     */
    List<NameInfo> findByTid(Integer tid);

    void save(NameInfo nameInfo);

    List<NameInfo> findAll();
}
