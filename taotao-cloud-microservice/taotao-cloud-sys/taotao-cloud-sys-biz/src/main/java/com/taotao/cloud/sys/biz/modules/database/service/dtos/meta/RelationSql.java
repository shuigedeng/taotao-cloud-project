package com.taotao.cloud.sys.biz.modules.database.service.dtos.meta;

import lombok.Data;

@Data
public class RelationSql {
    private String sql;
    private TableRelation.RelationEnum relation;
}
