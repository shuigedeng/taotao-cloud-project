package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename;


import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.JavaBeanInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMetaData;

public interface RenameStrategy {
    JavaBeanInfo mapping(TableMetaData tableMetaData);
}
