package com.taotao.cloud.sys.biz.tools.core.service.classloader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 远程仓库配置
 */
@ConfigurationProperties(prefix = "maven.config")
@Component
public class RemoteRepositoryConfig {

	/**
	 * 远程仓库配置
	 */
	private List<RemoteRepository> repositories = new ArrayList<>();

	public static final class RemoteRepository {
		private String id;
		private String type;
		private String url;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	public List<RemoteRepository> getRepositories() {
		return repositories;
	}

	public void setRepositories(
		List<RemoteRepository> repositories) {
		this.repositories = repositories;
	}
}
