package com.taotao.cloud.sys.biz.tools.mybatis.dtos;

import com.taotao.cloud.sys.biz.tools.database.dtos.DynamicQueryDto;
import org.apache.ibatis.mapping.SqlCommandType;

public class BoundSqlResponse {
    private SqlCommandType sqlCommandType;
    private DynamicQueryDto dynamicQueryDto;

    public BoundSqlResponse() {
    }

    public BoundSqlResponse(SqlCommandType sqlCommandType, DynamicQueryDto dynamicQueryDto) {
        this.sqlCommandType = sqlCommandType;
        this.dynamicQueryDto = dynamicQueryDto;
    }

	public SqlCommandType getSqlCommandType() {
		return sqlCommandType;
	}

	public void setSqlCommandType(SqlCommandType sqlCommandType) {
		this.sqlCommandType = sqlCommandType;
	}

	public DynamicQueryDto getDynamicQueryDto() {
		return dynamicQueryDto;
	}

	public void setDynamicQueryDto(
		DynamicQueryDto dynamicQueryDto) {
		this.dynamicQueryDto = dynamicQueryDto;
	}
}
