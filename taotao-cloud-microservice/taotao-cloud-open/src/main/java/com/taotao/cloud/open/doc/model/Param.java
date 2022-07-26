package com.taotao.cloud.open.doc.model;

import lombok.Data;

import java.util.List;

/**
 * API方法参数信息
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:11:21
 */
@Data
public class Param {

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数中文名
     */
    private String cnName;

    /**
     * 参数描述
     */
    private String describe;

    /**
     * 参数对象里的属性
     */
    private List<Property> properties;

}
