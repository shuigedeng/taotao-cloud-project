package com.taotao.cloud.workflow.biz.common.model.visiual;

import lombok.Data;

/**
 *
 */
@Data
public class ColumnDataModel {
    private String searchList;
    private String columnList;
    private String defaultColumnList;
    private String sortList;
    private Integer type;
    private String defaultSidx;
    private String sort;
    private Boolean hasPage;
    private Integer pageSize;
    private String treeTitle;
    private String treeDataSource;
    private String treeDictionary;
    private String treeRelation;
    private String treePropsUrl;
    private String treePropsValue;
    private String treePropsChildren;
    private String treePropsLabel;
    private String groupField;
    private String btnsList;
    private String columnBtnsList;
    /**
     * 自定义按钮区
     */
    private String customBtnsList;
    /**
     * 列表权限
     */
    private Boolean useColumnPermission;
    /**
     * 表单权限
     */
    private Boolean useFormPermission;
    /**
     * 按钮权限
     */
    private Boolean useBtnPermission;
    /**
     * 数据权限
     */
    private Boolean useDataPermission;
}
