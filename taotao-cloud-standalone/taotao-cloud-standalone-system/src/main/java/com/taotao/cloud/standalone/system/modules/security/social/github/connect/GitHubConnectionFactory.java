package com.taotao.cloud.standalone.system.modules.security.social.github.connect;

import com.taotao.cloud.standalone.system.modules.security.social.github.api.GitHub;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 21:49
 * @Version 1.0
 */
public class GitHubConnectionFactory extends OAuth2ConnectionFactory<GitHub> {

	public GitHubConnectionFactory(String providerId, String clientId, String clientSecret) {
		super(providerId, new GitHubServiceProvider(clientId, clientSecret), new GitHubAdapter());
	}
}
