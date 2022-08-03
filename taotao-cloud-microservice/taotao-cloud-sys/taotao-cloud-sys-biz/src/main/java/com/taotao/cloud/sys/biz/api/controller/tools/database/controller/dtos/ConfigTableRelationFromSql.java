package com.taotao.cloud.sys.biz.api.controller.tools.database.controller.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.RelationSql;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigTableRelationFromSql {
    private String connName;
    private Namespace namespace;
    private List<RelationSql> relationSqls = new ArrayList<>();
}
