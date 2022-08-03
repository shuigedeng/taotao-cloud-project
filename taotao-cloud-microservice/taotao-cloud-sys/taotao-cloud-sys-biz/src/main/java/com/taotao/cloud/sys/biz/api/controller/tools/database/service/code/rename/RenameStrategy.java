package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename;



public interface RenameStrategy {
    JavaBeanInfo mapping(TableMetaData tableMetaData);
}
