package com.taotao.cloud.sys.biz.modules.database.controller.dtos;

import com.sanri.tools.modules.database.service.dtos.meta.TableRelation;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
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
