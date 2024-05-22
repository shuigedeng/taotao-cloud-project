package com.taotao.cloud.mq.broker.dto;


import com.taotao.cloud.mq.common.rpc.RpcAddress;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ServiceEntry extends RpcAddress {

	/**
	 * 分组名称
	 */
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "ServiceEntry{" +
			"groupName='" + groupName + '\'' +
			"} " + super.toString();
	}

}
