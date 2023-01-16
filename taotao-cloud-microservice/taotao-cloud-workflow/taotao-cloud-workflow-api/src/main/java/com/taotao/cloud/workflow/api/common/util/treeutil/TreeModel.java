package com.taotao.cloud.workflow.api.common.util.treeutil;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 需要实现树的类可以继承该类，手写set方法，在设定本身属性值时同时设置该类中的相关属性
 */
@Data
public class TreeModel<T> {
    @Schema(description =  "主键")
    private String id;
    @Schema(description =  "名称")
    private String fullName;
    @Schema(description =  "父主键")
    private String parentId;
    @Schema(description =  "是否有下级菜单")
    private Boolean hasChildren = true;
    @Schema(description =  "图标")
    private String icon;
    @Schema(description =  "下级菜单列表")
    private List<TreeModel<T>> children = new ArrayList<>();
}
