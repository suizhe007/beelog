package com.zero.tech.data.db.mapper;

import com.zero.tech.data.db.domain.MonitorTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitorTempleteMapper {

    List<MonitorTemplate> findAll();
}
