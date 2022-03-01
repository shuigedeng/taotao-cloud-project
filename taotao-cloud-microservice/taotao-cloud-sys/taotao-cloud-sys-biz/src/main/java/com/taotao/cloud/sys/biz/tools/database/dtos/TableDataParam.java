package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class TableDataParam {
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    @Positive
    private int size = 1000;
    @Valid
    private List<ColumnMapper> columnMappers;

    public static class ColumnMapper{
        @NotNull
        private String columnName;
        // 随机方法 , 使用 spel 表达式
        private String random;
        // 使用 sql 的方式, 我会取第 0 个字段,然后取 100 数据,然后随便选择一条数据插入; 这种在有关联关系将会很有用
        private String sql;

	    public String getColumnName() {
		    return columnName;
	    }

	    public void setColumnName(String columnName) {
		    this.columnName = columnName;
	    }

	    public String getRandom() {
		    return random;
	    }

	    public void setRandom(String random) {
		    this.random = random;
	    }

	    public String getSql() {
		    return sql;
	    }

	    public void setSql(String sql) {
		    this.sql = sql;
	    }
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<ColumnMapper> getColumnMappers() {
		return columnMappers;
	}

	public void setColumnMappers(
		List<ColumnMapper> columnMappers) {
		this.columnMappers = columnMappers;
	}
}
