package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class DataQueryParam {
    @NotNull
    private String connName;
    private List<String> sqls;
    private String traceId;

    public String getFirstSql(){
        if (CollectionUtils.isNotEmpty(sqls)){
            return sqls.get(0);
        }
        throw new ToolException("必须要有一句 sql 做为查询条件");
    }

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public List<String> getSqls() {
		return sqls;
	}

	public void setSqls(List<String> sqls) {
		this.sqls = sqls;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
}
