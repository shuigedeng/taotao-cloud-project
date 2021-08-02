/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户实体绑定spring security
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:27
 */
public class SecurityMenu implements Serializable {

	private static final long serialVersionUID = -749360940290141180L;

	private String url;
	private String path;
	private Integer sort;
	private Integer type;
	private Boolean hidden;
	private String pathMethod;

	public SecurityMenu() {
	}

	public SecurityMenu(String url, String path, Integer sort, Integer type, Boolean hidden,
		String pathMethod) {
		this.url = url;
		this.path = path;
		this.sort = sort;
		this.type = type;
		this.hidden = hidden;
		this.pathMethod = pathMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getPathMethod() {
		return pathMethod;
	}

	public void setPathMethod(String pathMethod) {
		this.pathMethod = pathMethod;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SecurityMenu that = (SecurityMenu) o;
		return Objects.equals(url, that.url) && Objects.equals(path, that.path)
			&& Objects.equals(sort, that.sort) && Objects.equals(type,
			that.type) && Objects.equals(hidden, that.hidden) && Objects.equals(
			pathMethod, that.pathMethod);
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, path, sort, type, hidden, pathMethod);
	}

	@Override
	public String toString() {
		return "SecurityMenu{" +
			"url='" + url + '\'' +
			", path='" + path + '\'' +
			", sort=" + sort +
			", type=" + type +
			", hidden=" + hidden +
			", pathMethod='" + pathMethod + '\'' +
			'}';
	}

	public static SecurityMenuBuilder builder() {
		return new SecurityMenuBuilder();
	}

	public static final class SecurityMenuBuilder {

		private String url;
		private String path;
		private Integer sort;
		private Integer type;
		private Boolean hidden;
		private String pathMethod;

		private SecurityMenuBuilder() {
		}

		public static SecurityMenuBuilder aSecurityMenu() {
			return new SecurityMenuBuilder();
		}

		public SecurityMenuBuilder url(String url) {
			this.url = url;
			return this;
		}

		public SecurityMenuBuilder path(String path) {
			this.path = path;
			return this;
		}

		public SecurityMenuBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public SecurityMenuBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public SecurityMenuBuilder hidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public SecurityMenuBuilder pathMethod(String pathMethod) {
			this.pathMethod = pathMethod;
			return this;
		}

		public SecurityMenu build() {
			SecurityMenu securityMenu = new SecurityMenu();
			securityMenu.setUrl(url);
			securityMenu.setPath(path);
			securityMenu.setSort(sort);
			securityMenu.setType(type);
			securityMenu.setHidden(hidden);
			securityMenu.setPathMethod(pathMethod);
			return securityMenu;
		}
	}
}
