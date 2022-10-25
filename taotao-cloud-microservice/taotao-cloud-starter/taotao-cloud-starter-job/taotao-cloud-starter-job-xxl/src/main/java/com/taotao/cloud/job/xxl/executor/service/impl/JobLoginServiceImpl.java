package com.taotao.cloud.job.xxl.executor.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.xxl.executor.service.JobLoginService;
import com.taotao.cloud.job.xxl.properties.XxlJobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 工作登录服务实现类
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:44:54
 */
@Service
public class JobLoginServiceImpl implements JobLoginService {
	@Autowired
	private XxlJobProperties xxlJobProperties;

	private final Map<String, String> loginCookie = new HashMap<>();

	@Override
	public void login() {
		if (StrUtil.isBlank(xxlJobProperties.getAdmin().getAddresses())) {
			throw new RuntimeException("xxl admin address url 不能为空");
		}

		String url = xxlJobProperties.getAdmin().getAddresses() + "/login";
		HttpResponse response = HttpRequest.post(url)
			.form("userName", xxlJobProperties.getAdmin().getUsername())
			.form("password", xxlJobProperties.getAdmin().getPassword())
			.execute();
		List<HttpCookie> cookies = response.getCookies();
		Optional<HttpCookie> cookieOpt = cookies.stream()
			.filter(cookie -> "XXL_JOB_LOGIN_IDENTITY".equals(cookie.getName())).findFirst();
		if (cookieOpt.isEmpty()) {
			LogUtils.info("get xxl-job cookie error!");
			throw new RuntimeException("get xxl-job cookie error!");
		}

		String value = cookieOpt.get().getValue();
		loginCookie.put("XXL_JOB_LOGIN_IDENTITY", value);
	}

	@Override
	public String getCookie() {
		for (int i = 0; i < 3; i++) {
			String cookieStr = loginCookie.get("XXL_JOB_LOGIN_IDENTITY");
			if (cookieStr != null) {
				return "XXL_JOB_LOGIN_IDENTITY=" + cookieStr;
			}
			try {
				login();
			} catch (Exception e) {
				LogUtils.error("获取xxljob cookieStr 错误, 次数: {}", i);
			}
		}
		throw new RuntimeException("get xxl-job cookie error!");
	}


}
