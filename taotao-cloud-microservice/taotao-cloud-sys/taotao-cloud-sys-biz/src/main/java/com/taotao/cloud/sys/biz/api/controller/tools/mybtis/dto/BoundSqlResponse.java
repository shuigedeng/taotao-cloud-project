package com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.DynamicQueryDto;
import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

@Data
public class BoundSqlResponse {
    private SqlCommandType sqlCommandType;
    private DynamicQueryDto dynamicQueryDto;

    public BoundSqlResponse() {
    }

    public BoundSqlResponse(SqlCommandType sqlCommandType, DynamicQueryDto dynamicQueryDto) {
        this.sqlCommandType = sqlCommandType;
        this.dynamicQueryDto = dynamicQueryDto;
    }
}
