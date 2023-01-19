package com.taotao.cloud.workflow.biz.common.database.model;

import java.util.List;
import lombok.Data;

/**
 * jdbc分页模型
 *
 */
@Data
public class JdbcPageMod<T> {

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 数据总条数
     */
    private Integer totalRecord;

    /**
     * 数据
     */
    private List<T> dataList;

}
