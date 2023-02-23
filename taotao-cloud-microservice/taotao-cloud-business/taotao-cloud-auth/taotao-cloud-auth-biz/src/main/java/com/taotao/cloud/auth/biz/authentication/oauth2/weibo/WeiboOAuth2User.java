package com.taotao.cloud.auth.biz.authentication.oauth2.weibo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeiboOAuth2User implements OAuth2User {

	private String id;
	private String name;
	private String province;

	private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
	private Map<String, Object> attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (attributes == null) {
			attributes = new HashMap<>();

			attributes.put("id", this.getId());
			attributes.put("name", this.getName());
			attributes.put("province", this.getProvince());
		}

		return attributes;
	}
}
