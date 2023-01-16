package com.taotao.cloud.workflow.api.common.util.treeutil;

import java.util.Map;
import lombok.Data;

/**
 *
 */
@Data
public class TreeListModel {
    /**
     * 主键
     */
    private String id;
    /**
     * 名称
     */
    private String text;
    /**
     * 节点
     */
    private String parentId;
    /**
     * 表示此节点是否展开
     */
    private Boolean expanded;
    /**
     * 表示是否加载完成
     */
    private Boolean loaded;
    /**
     * 表示此数据是否为叶子节点
     */
    private Boolean isLeaf;
    /**
     * 表示此数据在哪一级
     */
    private Integer level;
    /**
     * 存储对象
     */
    private Map<String, Object> ht;
}
