package com.taotao.cloud.im.biz.platform.common.web.domain;

import lombok.Data;

/**
 * 动态查询器
 */
@Data
public class SearchVo {

    /**
     * 字段名
     */
    private String name;
    /**
     * 字段条件
     */
    private String condition;
    /**
     * 字段值
     */
    private String value;
    /**
     * 字段值
     */
    private String value2;

}
