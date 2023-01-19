package com.taotao.cloud.workflow.biz.common.util.treeutil;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 */
@Data
public class TreeViewModel {
    @Schema(description =  "主键")
    private String id;
    private String code;
    @Schema(description =  "名称")
    private String text;
    private String title;
    @Schema(description =  "父主键")
    private String parentId;
    @Schema(description =  "选中")
    private Integer checkstate;
    private Boolean showcheck = true;
    private Boolean isexpand = true;
    private Boolean complete = true;
    @Schema(description =  "图标")
    private String img;
    @Schema(description =  "样式")
    private String cssClass;
    @Schema(description =  "是否有下级菜单")
    private Boolean hasChildren;
    @Schema(description =  "其他")
    private Map<String, Object> ht;
    @Schema(description =  "是否点击")
    private Boolean click;
    @Schema(description =  "下级菜单列表")
    private List<TreeViewModel> childNodes;
}
