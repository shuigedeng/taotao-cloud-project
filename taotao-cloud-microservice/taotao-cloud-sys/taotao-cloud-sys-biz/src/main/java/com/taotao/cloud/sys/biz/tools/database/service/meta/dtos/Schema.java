package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;


public class Schema {
    private String schema;
    private String catalog;

    public Schema() {
    }

    public Schema(String schema, String catalog) {
        this.schema = schema;
        this.catalog = catalog;
    }

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}
