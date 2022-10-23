package com.taotao.cloud.job.xxl.executor.service;


import com.taotao.cloud.job.xxl.executor.model.XxlJobInfo;
import java.util.List;

public interface JobInfoService {

	List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

	Integer addJobInfo(XxlJobInfo xxlJobInfo);

}
