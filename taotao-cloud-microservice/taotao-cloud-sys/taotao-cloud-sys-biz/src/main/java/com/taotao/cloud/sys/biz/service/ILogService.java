/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.dto.log.LogQueryCriteria;
import com.taotao.cloud.sys.biz.entity.system.Log;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

/**
 * LogService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:19:55
 */
public interface ILogService extends IService<Log> {

	/**
	 * 分页获取日志数据
	 *
	 * @param nickname 昵称
	 * @param pageable 分页参数
	 * @return 日志数据
	 * @since 2022-03-23 08:54:49
	 */
	Object findAllByPageable(String nickname, Pageable pageable);

	/**
	 * 分页查询
	 *
	 * @param criteria 查询条件
	 * @param pageable 分页参数
	 * @return 日志数据
	 */
	Object queryAll(LogQueryCriteria criteria, Pageable pageable);

	/**
	 * 查询全部数据
	 *
	 * @param criteria 查询条件
	 * @return 日志数据列表
	 */
	List<Log> queryAll(LogQueryCriteria criteria);

	/**
	 * 查询用户日志
	 *
	 * @param criteria 查询条件
	 * @param pageable 分页参数
	 * @return 日志数据
	 */
	Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable);

	/**
	 * 保存日志数据
	 *
	 * @param username  用户
	 * @param ip        请求IP
	 * @param joinPoint /
	 * @param log       日志实体
	 */
	@Async
	void save(String username, String ip, ProceedingJoinPoint joinPoint, Log log, Long uid);

	/**
	 * 查询异常详情
	 *
	 * @param id 日志ID
	 * @return Object
	 */
	Object findByErrDetail(Long id);

	/**
	 * 导出日志
	 *
	 * @param logs     待导出的数据
	 * @param response /
	 * @throws IOException /
	 */
	void download(List<Log> logs, HttpServletResponse response) throws IOException;

	/**
	 * 删除所有错误日志
	 */
	void delAllByError();

	/**
	 * 删除所有INFO日志
	 */
	void delAllByInfo();
}
