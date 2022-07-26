package com.taotao.cloud.open.openapi.doc.model;

import lombok.Data;

import java.util.List;

/**
 * API方法参数信息
 *
 * @author wanghuidong
 * 时间： 2022/6/21 21:42
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
