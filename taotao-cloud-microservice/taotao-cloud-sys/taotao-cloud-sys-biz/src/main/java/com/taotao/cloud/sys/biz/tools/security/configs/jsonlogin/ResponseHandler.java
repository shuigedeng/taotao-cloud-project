package com.taotao.cloud.sys.biz.tools.security.configs.jsonlogin;

import com.sanri.tools.modules.core.dtos.ResponseDto;
import com.sanri.tools.modules.core.exception.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class ResponseHandler  {
	@Autowired
	protected MappingJackson2HttpMessageConverter messageConverter;

	/**
	 * 登录成功
	 * @param token
	 * @param response
	 * @throws IOException
	 */
	public void writeTokenAndAuthenticationSuccess(String token, HttpServletResponse response) throws IOException {
		response.setHeader("Authorization", token);
		writeMessage(ResponseDto.ok().data(token),response);
	}

	/**
	 * 写出消息
	 * @param response
	 * @throws IOException
	 */
	public void writeSuccess(HttpServletResponse response) throws IOException {
		writeMessage(SystemMessage.OK.result(),response);
	}

	/**
	 * 写出消息
	 * @param responseDto
	 * @param response
	 * @throws IOException
	 */
	public void writeMessage(ResponseDto responseDto,HttpServletResponse response) throws IOException {
		final ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
		messageConverter.write(responseDto, MediaType.APPLICATION_JSON_UTF8,servletServerHttpResponse);
	}

	/**
	 * 写出登录失败
	 * @param authenticationException
	 * @param response
	 */
	public void writeLoginFail(AuthenticationException authenticationException,HttpServletResponse response) throws IOException {
		writeMessage(SystemMessage.LOGIN_FAIL.result("用户名密码错误"), response);
	}

	/**
	 * 登录失败
	 * @param authenticationException
	 * @param response
	 * @throws IOException
	 */
	public void writeAuthenticationFail(AuthenticationException authenticationException,HttpServletResponse response) throws IOException {
		log.warn("异常信息:" + authenticationException.getClass().getSimpleName(),authenticationException.getCause());
		writeMessage(SystemMessage.NOT_LOGIN.result(),response);
	}

	/**
	 * 授权失败
	 * @param accessDeniedException
	 * @param response
	 * @throws IOException
	 */
	public void writeAuthorizationFail(AccessDeniedException accessDeniedException, HttpServletResponse response) throws IOException {
		writeMessage(SystemMessage.ACCESS_DENIED.result(),response);
	}
}
