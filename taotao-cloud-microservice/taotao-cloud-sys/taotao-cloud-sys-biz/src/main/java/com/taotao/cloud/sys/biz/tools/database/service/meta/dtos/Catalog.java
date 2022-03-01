package com.taotao.cloud.sys.biz.tools.database.service.meta.dtos;


import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private String catalog;
    private List<String> schemas = new ArrayList<>();

    public Catalog() {
    }

    public Catalog(String catalog) {
        this.catalog = catalog;
    }

    public Catalog(String catalog, List<String> schemas) {
        this.catalog = catalog;
        this.schemas = schemas;
    }

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
}
