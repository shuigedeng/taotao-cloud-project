package com.taotao.cloud.sys.biz.tools.soap.dtos;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 功能: webservice 类型 <br/>
 */
public class WsdlType {
	//类型名称
	private String typeName;
	private boolean simple;
	private List<WsdlParam> childParams;
	
	public void addChildParam(WsdlParam wsdlParam){
		if(childParams == null){
			childParams = new ArrayList<WsdlParam>();
		}
		this.childParams.add(wsdlParam);
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	public List<WsdlParam> getChildParams() {
		return childParams;
	}

	public void setChildParams(
		List<WsdlParam> childParams) {
		this.childParams = childParams;
	}
}
