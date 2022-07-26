package com.taotao.cloud.open.openapi.doc.model;

import lombok.Data;

import java.util.List;

/**
 * API方法返回值
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:11:27
 */
@Data
public class RetVal {

    /**
     * 返回值类型
     */
    private String retType;

    /**
     * 返回值中文名
     */
    private String cnName;

    /**
     * 返回值描述
     */
    private String describe;


    /**
     * 返回值里的属性
     */
    private List<Property> properties;
}
