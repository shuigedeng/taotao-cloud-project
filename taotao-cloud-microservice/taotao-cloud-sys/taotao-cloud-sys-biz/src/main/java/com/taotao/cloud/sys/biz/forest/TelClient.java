package com.taotao.cloud.sys.biz.forest;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.extensions.BasicAuth;
import com.dtflys.forest.extensions.OAuth2;

/**
 * 电信接口客户端
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-07 08:51:47
 */
@BaseRequest(baseURL = "http://gitee:8080/hello")
public interface TelClient {

	@Post("/hello/user?username={username}")
	@BasicAuth(username = "{username}", password = "bar")
	String getAccessToken(@Var("username") String username);

	@OAuth2(
		tokenUri = "/auth/oauth/token",
		clientId = "password",
		clientSecret = "xxxxx-yyyyy-zzzzz",
		grantType = OAuth2.GrantType.PASSWORD,
		scope = "any",
		username = "root",
		password = "xxxxxx"
	)
	@Get("/test/data")
	String getData();
}
