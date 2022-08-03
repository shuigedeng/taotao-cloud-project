package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta;

import lombok.Data;

@Data
public class RelationSql {
    private String sql;
    private TableRelation.RelationEnum relation;
}
