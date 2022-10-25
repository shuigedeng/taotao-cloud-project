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
import com.taotao.cloud.job.xxl.properties.XxlJobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作信息服务实现类
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:44:47
 */
@Service
public class JobInfoServiceImpl implements JobInfoService {

	@Autowired
	private XxlJobProperties xxlJobProperties;

	@Autowired
	private JobLoginService jobLoginService;

	@Override
	public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobinfo/pageList";
		HttpResponse response = HttpRequest.post(url)
			.form("jobGroup", jobGroupId)
			.form("executorHandler", executorHandler)
			.form("triggerStatus", -1)
			.cookie(jobLoginService.getCookie())
			.execute();

		String body = response.body();
		JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);

		return array.stream()
			.map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
			.collect(Collectors.toList());
	}

	@Override
	public Integer addJobInfo(XxlJobInfo xxlJobInfo) {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobinfo/add";
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
