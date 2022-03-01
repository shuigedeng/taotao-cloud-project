package com.taotao.cloud.sys.biz.tools.core.service.storage;


import java.util.ArrayList;
import java.util.List;

public class ConfigTree {
    private String name;
    private List<ConfigTree> childes = new ArrayList<>();
    private String path;
    private ConfigInfo origin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConfigTree> getChildes() {
		return childes;
	}

	public void setChildes(
		List<ConfigTree> childes) {
		this.childes = childes;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ConfigInfo getOrigin() {
		return origin;
	}

	public void setOrigin(ConfigInfo origin) {
		this.origin = origin;
	}
}
