package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据表标记
 * 可以打上配置表,业务表,统计表等标签
 */
public class TableMark {
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    private Set<String> tags = new HashSet<>();

    public TableMark() {
    }

    public TableMark(String connName, ActualTableName actualTableName) {
        this.connName = connName;
        this.actualTableName = actualTableName;
    }

    /**
     * 表是否弃用
     * @return
     */
    public boolean isDeprecated(){
        return this.tags != null && tags.contains("deprecated");
    }

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
}
