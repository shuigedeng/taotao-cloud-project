package com.taotao.cloud.job.xxl.executor.service;


import com.taotao.cloud.job.xxl.executor.model.XxlJobGroup;

import java.util.List;

/**
 * 工作小组服务
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:45:06
 */
public interface JobGroupService {

	/**
	 * 得到工作小组
	 *
	 * @return {@link List }<{@link XxlJobGroup }>
	 * @since 2022-10-25 09:45:06
	 */
	List<XxlJobGroup> getJobGroup();

	/**
	 * 汽车登记集团
	 *
	 * @return boolean
	 * @since 2022-10-25 09:45:06
	 */
	boolean autoRegisterGroup();

	/**
	 * 精确检查
	 *
	 * @return boolean
	 * @since 2022-10-25 09:45:06
	 */
	boolean preciselyCheck();

}
