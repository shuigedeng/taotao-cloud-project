package com.taotao.cloud.sys.biz.tools.core.dtos;


import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class SpiderDataParam {
    /**
     * 类名
     */
    @NotNull
    private String className;
    /**
     * 类加载器名称
     */
    @NotNull
    private String classloaderName;
    /**
     * 其它参数
     */
    private Map<String,String> params = new HashMap<>();

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
