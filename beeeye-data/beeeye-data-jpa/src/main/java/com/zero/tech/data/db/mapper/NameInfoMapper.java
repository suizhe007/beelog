package com.zero.tech.data.db.mapper;

import com.zero.tech.data.db.domain.NameInfo;
import com.zero.tech.data.db.dto.NameInfoDto;

import java.util.List;

public interface NameInfoMapper {
    List<NameInfoDto> findByType(String type);

    List<NameInfoDto> findByTypeName(String type, String name);

    /**
     * 查询属于tid模板的所有name info
     *
     * @param tid
     * @return
     */
    List<NameInfo> findByTid(Integer tid);

    void save(NameInfo nameInfo);

    Iterable<NameInfo> findAll();
}
