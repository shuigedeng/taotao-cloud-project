package com.taotao.cloud.java.javaee.s2.c11_distributed.item.java.mapper;

import org.apache.ibatis.annotations.Update;

public interface ItemMapper {


    @Update("update item set stock = stock - 1 where id = 1")
    void update();
}
