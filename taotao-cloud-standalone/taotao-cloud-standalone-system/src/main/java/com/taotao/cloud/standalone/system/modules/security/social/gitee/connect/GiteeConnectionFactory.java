package com.taotao.cloud.standalone.system.modules.security.social.gitee.connect;

import com.taotao.cloud.standalone.system.modules.security.social.gitee.api.Gitee;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 21:49
 * @Version 1.0
 */
public class GiteeConnectionFactory extends OAuth2ConnectionFactory<Gitee> {

	public GiteeConnectionFactory(String providerId, String clientId, String clientSecret) {
		super(providerId, new GiteeServiceProvider(clientId, clientSecret), new GiteeAdapter());
	}
}
