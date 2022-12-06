package com.taotao.cloud.security.springsecurity.perm;

import java.util.List;
import java.util.Objects;
import org.springframework.security.access.ConfigAttribute;

public class PermRoleEntity {

	/**
	 * 访问的接口
	 **/
	private String accessUri;
	/**
	 * 可访问该接口的角色集合
	 **/
	private List<ConfigAttribute> configAttributeList;

	public String getAccessUri() {
		return accessUri;
	}

	public void setAccessUri(String accessUri) {
		this.accessUri = accessUri;
	}

	public List<ConfigAttribute> getConfigAttributeList() {
		return configAttributeList;
	}

	public void setConfigAttributeList(
		List<ConfigAttribute> configAttributeList) {
		this.configAttributeList = configAttributeList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PermRoleEntity that = (PermRoleEntity) o;

		if (!Objects.equals(accessUri, that.accessUri)) {
			return false;
		}
		return Objects.equals(configAttributeList, that.configAttributeList);
	}

	@Override
	public int hashCode() {
		int result = accessUri != null ? accessUri.hashCode() : 0;
		result = 31 * result + (configAttributeList != null ? configAttributeList.hashCode() : 0);
		return result;
	}
}
