package com.taotao.cloud.job.xxl.executor.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.job.xxl.executor.model.XxlJobGroup;
import com.taotao.cloud.job.xxl.executor.service.JobGroupService;
import com.taotao.cloud.job.xxl.executor.service.JobLoginService;
import com.taotao.cloud.job.xxl.properties.XxlJobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工作集团服务实现类
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:44:42
 */
@Service
public class JobGroupServiceImpl implements JobGroupService {

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Autowired
	private JobLoginService jobLoginService;

	@Override
	public List<XxlJobGroup> getJobGroup() {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobgroup/pageList";
		HttpResponse response = HttpRequest.post(url)
			.form("appname", xxlJobProperties.getExecutor().getAppname())
			.form("title", xxlJobProperties.getExecutor().getTitle())
			.cookie(jobLoginService.getCookie())
			.execute();

		String body = response.body();
		JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);

		return array.stream()
			.map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
			.collect(Collectors.toList());
	}

	@Override
	public boolean autoRegisterGroup() {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobgroup/save";
		HttpResponse response = HttpRequest.post(url)
			.form("appname", xxlJobProperties.getExecutor().getAppname())
			.form("title", xxlJobProperties.getExecutor().getTitle())
			.cookie(jobLoginService.getCookie())
			.execute();
		Object code = JSONUtil.parse(response.body()).getByPath("code");
		return code.equals(200);
	}

	@Override
	public boolean preciselyCheck() {
		List<XxlJobGroup> jobGroup = getJobGroup();
		Optional<XxlJobGroup> has = jobGroup.stream()
			.filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(xxlJobProperties.getExecutor().getAppname())
				&& xxlJobGroup.getTitle().equals(xxlJobProperties.getExecutor().getTitle()))
			.findAny();
		return has.isPresent();
	}

}
