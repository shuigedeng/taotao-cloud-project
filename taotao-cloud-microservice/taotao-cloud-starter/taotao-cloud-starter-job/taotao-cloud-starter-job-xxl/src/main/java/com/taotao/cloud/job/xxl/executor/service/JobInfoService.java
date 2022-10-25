package com.taotao.cloud.job.xxl.executor.service;


import com.taotao.cloud.job.xxl.executor.model.XxlJobInfo;

import java.util.List;

/**
 * 就业信息服务
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:45:10
 */
public interface JobInfoService {

	/**
	 * 获得工作信息
	 *
	 * @param jobGroupId      工作组id
	 * @param executorHandler 遗嘱执行人处理程序
	 * @return {@link List }<{@link XxlJobInfo }>
	 * @since 2022-10-25 09:45:10
	 */
	List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

	/**
	 * 添加工作信息
	 *
	 * @param xxlJobInfo xxl工作信息
	 * @return {@link Integer }
	 * @since 2022-10-25 09:45:11
	 */
	Integer addJobInfo(XxlJobInfo xxlJobInfo);

}
