/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dto.log.LogQueryCriteria;
import com.taotao.cloud.sys.biz.entity.Log;
import com.taotao.cloud.sys.biz.mapper.LogMapper;
import com.taotao.cloud.sys.biz.service.LogService;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * LogServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 08:55:37
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements
	LogService {

	@Override
	public Object findAllByPageable(String nickname, Pageable pageable) {
		//getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(this.baseMapper.findAllByPageable(nickname));
		Map<String, Object> map = new LinkedHashMap<>(2);
		map.put("content", page.getList());
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {
		//getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		String status = "ERROR";
		if (status.equals(criteria.getLogType())) {
			//map.put("content", generator.convert(page.getList(), LogErrorDTO.class));
			map.put("totalElements", page.getTotal());
		}
		map.put("content", page.getList());
		map.put("totalElements", page.getTotal());
		return map;
	}

	@Override
	public List<Log> queryAll(LogQueryCriteria criteria) {
		return baseMapper.selectList(new QueryWrapper<>(new Log()));
		//return baseMapper.selectList(QueryHelpPlus.getPredicate(Log.class, criteria));
	}

	@Override
	public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
		//getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		//map.put("content", generator.convert(page.getList(), LogSmallDTO.class));
		map.put("totalElements", page.getTotal());
		return map;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(String username, String ip, ProceedingJoinPoint joinPoint, Log log, Long uid) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RequestLogger aopLog = method.getAnnotation(RequestLogger.class);

		// 方法路径
		String methodName =
			joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

		StringBuilder params = new StringBuilder("{");
		//参数值
		Object[] argValues = joinPoint.getArgs();
		//参数名称
		String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		if (argValues != null) {
			for (int i = 0; i < argValues.length; i++) {
				params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
			}
		}
		// 描述
		if (log != null) {
			log.setDescription(aopLog.description());
		}
		//类型 0-后台 1-前台
		//log.setType(aopLog.getType());
		if (uid != null) {
			log.setUid(uid);
		}
		assert log != null;
		log.setRequestIp(ip);

		String loginPath = "login";
		if (loginPath.equals(signature.getName())) {
			try {
				assert argValues != null;
				username = new JSONObject(argValues[0]).get("username").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.setAddress(StringUtil.getCityInfo(log.getRequestIp()));
		log.setMethod(methodName);
		log.setUsername(username);
		log.setParams(params.toString() + " }");
		this.save(log);
	}

	@Override
	public Object findByErrDetail(Long id) {
		Log log = this.getById(id);
		//ValidationUtil.isNull(log.getId(), "Log", "id", id);
		byte[] details = log.getExceptionDetail();
		return Dict.create()
			.set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
	}

	@Override
	public void download(List<Log> logs, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Log log : logs) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("用户名", log.getUsername());
			map.put("IP", log.getRequestIp());
			map.put("IP来源", log.getAddress());
			map.put("描述", log.getDescription());
			map.put("浏览器", log.getBrowser());
			map.put("请求耗时/毫秒", log.getTime());
			map.put("异常详情", new String(
				ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail()
					: "".getBytes()));
			map.put("创建日期", log.getCreateTime());
			list.add(map);
		}
		//FileUtil.downloadExcel(list, response);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delAllByError() {
		this.baseMapper.deleteByLogType("ERROR");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delAllByInfo() {
		this.baseMapper.deleteByLogType("INFO");
	}
}
