package com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.mapper;

import org.apache.ibatis.annotations.Insert;

public interface OrderMapper {

    @Insert("insert into `order` (id,name,money) values (1,'张三',123)")
    void save();
}
