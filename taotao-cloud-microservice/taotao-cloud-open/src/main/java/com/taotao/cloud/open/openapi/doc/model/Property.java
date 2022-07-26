package com.taotao.cloud.open.openapi.doc.model;

import lombok.Data;

import java.util.List;

/**
 * 对象属性信息
 *
 * @author wanghuidong
 * 时间： 2022/6/22 10:58
 */
@Data
public class Property {

    /**
     * 属性类型
     */
    private String type;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性中文名
     */
    private String cnName;

    /**
     * 属性描述
     */
    private String describe;

    /**
     * 属性里的属性
     */
    private List<Property> properties;
}
