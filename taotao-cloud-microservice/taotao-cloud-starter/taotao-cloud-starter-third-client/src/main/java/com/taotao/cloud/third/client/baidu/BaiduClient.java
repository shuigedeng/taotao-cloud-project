package com.taotao.cloud.third.client.baidu;

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
public interface BaiduClient {

	@Post("/hello/user?username={username}")
	@BasicAuth(username = "{username}", password = "bar")
	String getAccessToken(@Var("username") String username);

	@Get("https://apis.map.qq.com/ws/geocoder/v1/?location=29.57,106.55&key=ZE6BZ-HH33P-IU4DI-VL2H3-VEEIS-35FFH")
	String getData();
}
