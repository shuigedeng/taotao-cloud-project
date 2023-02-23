package com.taotao.cloud.workflow.biz.common.util.treeutil;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;


/**
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SumTree2<T> {
    private String id;
    private String parentId;
    private Boolean hasChildren;
    private List<T> children;
}
