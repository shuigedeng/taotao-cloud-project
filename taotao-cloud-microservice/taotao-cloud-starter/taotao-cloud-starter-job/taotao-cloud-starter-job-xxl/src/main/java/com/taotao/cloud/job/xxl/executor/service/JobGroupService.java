package com.taotao.cloud.job.xxl.executor.service;


import com.taotao.cloud.job.xxl.executor.model.XxlJobGroup;
import java.util.List;

public interface JobGroupService {

	List<XxlJobGroup> getJobGroup();

	boolean autoRegisterGroup();

	boolean preciselyCheck();

}
