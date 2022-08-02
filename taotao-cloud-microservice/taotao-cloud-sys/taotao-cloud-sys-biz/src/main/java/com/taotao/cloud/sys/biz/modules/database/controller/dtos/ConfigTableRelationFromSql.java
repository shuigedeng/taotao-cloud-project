package com.taotao.cloud.sys.biz.modules.database.controller.dtos;

import com.sanri.tools.modules.database.service.dtos.meta.RelationSql;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigTableRelationFromSql {
    private String connName;
    private Namespace namespace;
    private List<RelationSql> relationSqls = new ArrayList<>();
}
