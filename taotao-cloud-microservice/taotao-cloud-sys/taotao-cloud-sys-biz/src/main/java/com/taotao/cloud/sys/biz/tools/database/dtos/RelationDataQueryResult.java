package com.taotao.cloud.sys.biz.tools.database.dtos;


import java.util.List;

public class RelationDataQueryResult {
    private List<String> parents;
    private List<String> childs;
    private String sql;

    public RelationDataQueryResult() {
    }

    public RelationDataQueryResult(String sql) {
        this.sql = sql;
    }

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public List<String> getChilds() {
		return childs;
	}

	public void setChilds(List<String> childs) {
		this.childs = childs;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
