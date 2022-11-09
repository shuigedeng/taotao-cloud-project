package com.taotao.cloud.auth.biz.authentication.miniapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.auth.biz.authentication.miniapp.service.MiniAppClient;
import com.taotao.cloud.auth.biz.authentication.miniapp.service.MiniAppClientService;
import com.taotao.cloud.auth.biz.authentication.miniapp.service.MiniAppSessionKeyCacheService;
import com.taotao.cloud.auth.biz.authentication.miniapp.service.WechatLoginResponse;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * 小程序预授权
 */
public class MiniAppPreAuthenticationFilter extends OncePerRequestFilter {

	private static final String ENDPOINT = "https://api.weixin.qq.com/sns/jscode2session";
	private static final String MINI_CLIENT_KEY = "clientId";
	private static final String JS_CODE_KEY = "jsCode";
	private final RequestMatcher requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(
		"/login/miniapp/preauth", "GET");
	private final ObjectMapper om = new ObjectMapper();
	private final MiniAppClientService miniAppClientService;
	private final MiniAppSessionKeyCacheService miniAppSessionKeyCacheService;
	private final RestOperations restOperations;

	/**
	 * Instantiates a new Mini app pre authentication filter.
	 *
	 * @param miniAppClientService          the mini app client service
	 * @param miniAppSessionKeyCacheService the mini app session key cache
	 */
	public MiniAppPreAuthenticationFilter(MiniAppClientService miniAppClientService,
										  MiniAppSessionKeyCacheService miniAppSessionKeyCacheService) {
		this.miniAppClientService = miniAppClientService;
		this.miniAppSessionKeyCacheService = miniAppSessionKeyCacheService;
		this.restOperations = new RestTemplate();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		if (response.isCommitted()) {
			return;
		}

		if (requiresAuthenticationRequestMatcher.matches(request)) {
			String clientId = request.getParameter(MINI_CLIENT_KEY);
			String jsCode = request.getParameter(JS_CODE_KEY);
			MiniAppClient miniAppClient = miniAppClientService.get(clientId);
			WechatLoginResponse responseEntity = this.getResponse(miniAppClient, jsCode);

			String openId = responseEntity.getOpenid();
			String sessionKey = responseEntity.getSessionKey();
			miniAppSessionKeyCacheService.put(clientId + "::" + openId, sessionKey);
			responseEntity.setSessionKey(null);
			ResponseUtils.success(response, Result.success(responseEntity));
			return;
		}
		filterChain.doFilter(request, response);
	}

	// private static class PreAuthResponseWriter extends ResponseWriter {
	//
	// 	@Override
	// 	protected Map<String, Object> body(HttpServletRequest request) {
	// 		WechatLoginResponse miniAuth = (WechatLoginResponse) request.getAttribute(ATTRIBUTE_KEY);
	// 		Map<String, Object> map = new HashMap<>(3);
	// 		map.put("code", HttpStatus.OK.value());
	// 		map.put("data", miniAuth);
	// 		map.put("message", HttpStatus.OK.getReasonPhrase());
	// 		return map;
	// 	}
	// }


	/**
	 * 请求微信服务器登录接口 code2session
	 *
	 * @param miniAppClient miniAppClient
	 * @param jsCode        jsCode
	 * @return ObjectNode
	 */
	private WechatLoginResponse getResponse(MiniAppClient miniAppClient, String jsCode)
		throws JsonProcessingException {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("appid", miniAppClient.getAppId());
		queryParams.add("secret", miniAppClient.getSecret());
		queryParams.add("js_code", jsCode);
		queryParams.add("grant_type", "authorization_code");

		URI uri = UriComponentsBuilder.fromHttpUrl(ENDPOINT)
			.queryParams(queryParams)
			.build()
			.toUri();
		String response = restOperations.getForObject(uri, String.class);

		if (Objects.isNull(response)) {
			throw new BadCredentialsException("miniapp response is null");
		}
		return om.readValue(response, WechatLoginResponse.class);
	}
}
