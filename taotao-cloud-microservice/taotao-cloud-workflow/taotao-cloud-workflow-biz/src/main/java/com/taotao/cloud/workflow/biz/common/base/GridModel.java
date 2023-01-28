package com.taotao.cloud.workflow.biz.common.base;

import lombok.Data;

/**
 *
 */
@Data
public class GridModel {
    /**
     *  排序列
     */
    private String sidx;
    /**
     *  排序类型
     */
    private String sord;
    /**
     *  查询条件
     */
    private String queryJson;
    /**
     * 查询关键字
     */
    private String keyword;
}
