package com.taotao.cloud.sys.api.web.dto.jvm;

import javax.validation.constraints.NotBlank;

public class InvokeParam {
    /**
     * 开启了JMX的主机:端口
     */
    @NotBlank
    private String connName;
    /**
     * mBean 名称, 写 mBeanName 传参不进来, 不知道为啥, 所以换成了 beanName
     */
    @NotBlank
    private String beanName;
    /**
     * 方法名
     */
    @NotBlank
    private String operation;
    /**
     * 参数列表
     */
    private Object [] params = new Object[0];

    /**
     * 参数签名列表, 这个是方法的参数的 type 信息列表
     */
    private String [] signature = new String[0];

    public InvokeParam() {
    }

    public InvokeParam(@NotBlank String connName, @NotBlank String beanName, @NotBlank String operation) {
        this.connName = connName;
        this.beanName = beanName;
        this.operation = operation;
    }

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String[] getSignature() {
		return signature;
	}

	public void setSignature(String[] signature) {
		this.signature = signature;
	}
}
