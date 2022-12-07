package com.taotao.cloud.sa.just.biz.just;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.security.justauth.factory.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthController {

	private final AuthRequestFactory factory;

	@GetMapping
	public List<String> list() {
		return factory.oauthList();
	}

	@GetMapping("/login/{type}")
	public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
		AuthRequest authRequest = factory.get(type);
		response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
	}

	@RequestMapping("/{type}/callback")
	public AuthResponse login(@PathVariable String type, AuthCallback callback) {
		AuthRequest authRequest = factory.get(type);
		AuthResponse response = authRequest.login(callback);
		log.info("【response】= {}", JSONUtil.toJsonStr(response));

		StpUtil.login(10001);

		return response;
	}

}
