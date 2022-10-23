package com.taotao.cloud.job.xxl.executor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.job.xxl.executor.model.XxlJobInfo;
import com.taotao.cloud.job.xxl.executor.service.JobInfoService;
import com.taotao.cloud.job.xxl.executor.service.JobLoginService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author : Hydra
 * @date: 2022/9/20 10:36
 * @version: 1.0
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {

	@Value("${xxl.job.admin.addresses}")
	private String adminAddresses;

	@Autowired
	private JobLoginService jobLoginService;

	@Override
	public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
		String url = adminAddresses + "/jobinfo/pageList";
		HttpResponse response = HttpRequest.post(url)
			.form("jobGroup", jobGroupId)
			.form("executorHandler", executorHandler)
			.form("triggerStatus", -1)
			.cookie(jobLoginService.getCookie())
			.execute();

		String body = response.body();
		JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
		List<XxlJobInfo> list = array.stream()
			.map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
			.collect(Collectors.toList());

		return list;
	}

	@Override
	public Integer addJobInfo(XxlJobInfo xxlJobInfo) {
		String url = adminAddresses + "/jobinfo/add";
		Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
		HttpResponse response = HttpRequest.post(url)
			.form(paramMap)
			.cookie(jobLoginService.getCookie())
			.execute();

		JSON json = JSONUtil.parse(response.body());
		Object code = json.getByPath("code");
		if (code.equals(200)) {
			return Convert.toInt(json.getByPath("content"));
		}
		throw new RuntimeException("add jobInfo error!");
	}

}
