package com.taotao.cloud.sys.biz.tools.database.service;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import com.taotao.cloud.sys.biz.tools.database.service.rename.JavaBeanInfo;

public interface RenameStrategy {
    JavaBeanInfo mapping(TableMetaData tableMetaData);
}
