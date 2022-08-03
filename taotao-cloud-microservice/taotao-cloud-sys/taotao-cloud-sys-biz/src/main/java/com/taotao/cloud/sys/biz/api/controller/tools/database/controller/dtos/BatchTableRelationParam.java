package com.taotao.cloud.sys.biz.api.controller.tools.database.controller.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableRelation;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class BatchTableRelationParam {
    /**
     * 连接名
     */
    private String connName;
    /**
     * 命名空间
     */
    private Namespace namespace;

    /**
     * 表关系列表
     */
    @Valid
    private Set<TableRelation> tableRelations = new HashSet<>();
}
