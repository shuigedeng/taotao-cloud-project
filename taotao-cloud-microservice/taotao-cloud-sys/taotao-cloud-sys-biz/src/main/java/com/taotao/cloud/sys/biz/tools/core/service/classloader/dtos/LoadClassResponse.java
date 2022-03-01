package com.taotao.cloud.sys.biz.tools.core.service.classloader.dtos;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoadClassResponse {
    /**
     * 加载的类列表
     */
    private List<String> classes = new ArrayList<>();
    /**
     * 加载的 jar 列表
     */
    private List<String> jars = new ArrayList<>();

    /**
     * 无效文件列表
     */
    private List<String> removeFiles = new ArrayList<>();

    public void addClasses(Collection<String> classes){
        this.classes.addAll(classes);
    }

    public void addClass(String className){
        this.classes.add(className);
    }

    public void addJars(Collection<String> jars){
        this.jars.addAll(jars);
    }

    public void addRemoveFile(String filename){
        removeFiles.add(filename);
    }

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public List<String> getJars() {
		return jars;
	}

	public void setJars(List<String> jars) {
		this.jars = jars;
	}

	public List<String> getRemoveFiles() {
		return removeFiles;
	}

	public void setRemoveFiles(List<String> removeFiles) {
		this.removeFiles = removeFiles;
	}

}
