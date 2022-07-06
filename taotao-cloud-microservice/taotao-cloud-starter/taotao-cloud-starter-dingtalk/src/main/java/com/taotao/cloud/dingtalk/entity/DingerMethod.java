package com.taotao.cloud.dingtalk.entity;

/**
 * DingerMethod
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:19:28
 */
public class DingerMethod {

	String methodName;
	String[] methodParams;
	int[] paramTypes;

	public DingerMethod(String methodName, String[] methodParams, int[] paramTypes) {
		this.methodName = methodName;
		this.methodParams = methodParams;
		this.paramTypes = paramTypes;
	}

	public boolean check() {
		if (paramTypes == null) {
			return false;
		}

		int length = this.methodParams.length;
		for (int index : paramTypes) {
			if (index >= length) {
				return true;
			}
		}
		return false;
	}

	public String getMethodName() {
		return methodName;
	}

	public String[] getMethodParams() {
		return methodParams;
	}

	public int[] getParamTypes() {
		return paramTypes;
	}
}
