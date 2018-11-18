package com.zero.tech.data.db.mapper;

import java.util.List;

public interface ServiceInfoMapper {

    List<String> findAllIface();

    List<String> findMethodByIface(String iface);
}
