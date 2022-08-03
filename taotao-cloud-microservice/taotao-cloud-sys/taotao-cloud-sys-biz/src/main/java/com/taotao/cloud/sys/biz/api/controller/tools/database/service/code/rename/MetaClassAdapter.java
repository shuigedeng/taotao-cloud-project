package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * 数据库元数据到类信息的适配器
 */
public class MetaClassAdapter {
    private Converter<String, String> propertyConverter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
    private Converter<String, String> classNameConverter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);

    public String mappingClassName(String tableName){
        return classNameConverter.convert(tableName);
    }

    public String mappingProperty(String columnName){
        return propertyConverter.convert(columnName);
    }
}
