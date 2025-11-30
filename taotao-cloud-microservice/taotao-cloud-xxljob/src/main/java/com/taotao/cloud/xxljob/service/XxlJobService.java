package com.taotao.cloud.xxljob.service;


import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.model.XxlJobUser;
import com.xxl.tool.response.Response;
import com.xxl.sso.core.model.LoginInfo;

import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 * 
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {

	/**
	 * page list
	 *
	 * @param start
	 * @param length
	 * @param jobGroup
	 * @param jobDesc
	 * @param executorHandler
	 * @param author
	 * @return
	 */
	public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	public Response<String> add(XxlJobInfo jobInfo, LoginInfo loginInfo);

	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	public Response<String> update(XxlJobInfo jobInfo, LoginInfo loginInfo);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public Response<String> remove(int id, LoginInfo loginInfo);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	public Response<String> start(int id, LoginInfo loginInfo);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	public Response<String> stop(int id, LoginInfo loginInfo);

	/**
	 * trigger
	 *
	 * @param loginInfo
	 * @param jobId
	 * @param executorParam
	 * @param addressList
	 * @return
	 */
	public Response<String> trigger(LoginInfo loginInfo, int jobId, String executorParam, String addressList);

	/**
	 * dashboard info
	 *
	 * @return
	 */
	public Map<String,Object> dashboardInfo();

	/**
	 * chart info
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Response<Map<String,Object>> chartInfo(Date startDate, Date endDate);

}
