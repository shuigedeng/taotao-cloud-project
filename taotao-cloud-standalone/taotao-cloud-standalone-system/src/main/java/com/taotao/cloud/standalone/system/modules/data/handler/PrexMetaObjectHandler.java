package com.taotao.cloud.standalone.system.modules.data.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @Classname PrexMetaObjectHandler
 * @Description  字段自动填充器
 * @Author shuigedeng
 * @since 2019-11-13 16:25
 * 
 */
@Slf4j
@Component
public class PrexMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        //避免使用metaObject.setValue()
        this.setFieldValByName("delFlag", "0", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("operator", "Tom", metaObject);
    }
}
