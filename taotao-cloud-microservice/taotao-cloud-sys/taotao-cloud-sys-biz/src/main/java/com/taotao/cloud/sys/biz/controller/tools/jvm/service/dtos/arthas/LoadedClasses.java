package com.taotao.cloud.sys.biz.controller.tools.jvm.service.dtos.arthas;


import java.util.ArrayList;
import java.util.List;

/**
 * 所有加载的类信息
 */
public class LoadedClasses {

    private List<ClassInfo> classInfos = new ArrayList<>();

    public static final class ClassInfo{
        private String className;
        private String classLoaderName;

	    public String getClassName() {
		    return className;
	    }

	    public void setClassName(String className) {
		    this.className = className;
	    }

	    public String getClassLoaderName() {
		    return classLoaderName;
	    }

	    public void setClassLoaderName(String classLoaderName) {
		    this.classLoaderName = classLoaderName;
	    }
    }

    public static final class MethodInfo{
        private String methodName;
        private List<ParameterInfo> signature = new ArrayList<>();
        private String returnType;

	    public String getMethodName() {
		    return methodName;
	    }

	    public void setMethodName(String methodName) {
		    this.methodName = methodName;
	    }

	    public List<ParameterInfo> getSignature() {
		    return signature;
	    }

	    public void setSignature(
		    List<ParameterInfo> signature) {
		    this.signature = signature;
	    }

	    public String getReturnType() {
		    return returnType;
	    }

	    public void setReturnType(String returnType) {
		    this.returnType = returnType;
	    }
    }

    public static final class ParameterInfo{
        private String type;
        private String name;

	    public String getType() {
		    return type;
	    }

	    public void setType(String type) {
		    this.type = type;
	    }

	    public String getName() {
		    return name;
	    }

	    public void setName(String name) {
		    this.name = name;
	    }
    }

	public List<ClassInfo> getClassInfos() {
		return classInfos;
	}

	public void setClassInfos(
		List<ClassInfo> classInfos) {
		this.classInfos = classInfos;
	}
}
