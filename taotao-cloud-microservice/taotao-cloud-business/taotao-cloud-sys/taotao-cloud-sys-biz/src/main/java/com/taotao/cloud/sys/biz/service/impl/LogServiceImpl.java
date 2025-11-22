/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.webmvc.request.annotation.RequestLogger;
import com.taotao.cloud.sys.api.model.dto.LogQueryCriteria;
import com.taotao.cloud.sys.biz.mapper.ILogMapper;
import com.taotao.cloud.sys.biz.model.entity.Log;
import com.taotao.cloud.sys.biz.service.ILogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LogServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 08:55:37
 */
@Service
@AllArgsConstructor
public class LogServiceImpl extends ServiceImpl<ILogMapper, Log> implements ILogService {

	@Override
	public Object findAllByPageable(String nickname, Pageable pageable) {
		// getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(this.baseMapper.findAllByPageable(nickname));
		Map<String, Object> map = new LinkedHashMap<>(2);
		map.put("content", page.getList());
		map.put("totalElements", page.getTotal());
		return map;
	}

	@Override
	public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {
		// getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		String status = "ERROR";
		if (status.equals(criteria.getLogType())) {
			// map.put("content", generator.convert(page.getList(), LogErrorDTO.class));
			map.put("totalElements", page.getTotal());
		}
		map.put("content", page.getList());
		map.put("totalElements", page.getTotal());
		return map;
	}

	@Override
	public List<Log> queryAll(LogQueryCriteria criteria) {
		return baseMapper.selectList(new QueryWrapper<>(new Log()));
		// return baseMapper.selectList(QueryHelpPlus.getPredicate(Log.class, criteria));
	}

	@Override
	public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
		// getPage(pageable);
		PageInfo<Log> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		// map.put("content", generator.convert(page.getList(), LogSmallDTO.class));
		map.put("totalElements", page.getTotal());
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(String username, String ip, ProceedingJoinPoint joinPoint, Log log, Long uid) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RequestLogger requestLogger = method.getAnnotation(RequestLogger.class);

		// 方法路径
		String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

		StringBuilder params = new StringBuilder("{");
		// 参数值
		Object[] argValues = joinPoint.getArgs();
		// 参数名称
		String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		if (argValues != null) {
			for (int i = 0; i < argValues.length; i++) {
				params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
			}
		}
		// 描述
		if (log != null) {
			log.setDescription(requestLogger.value());
		}
		// 类型 0-后台 1-前台
		// log.setType(aopLog.getType());
		if (uid != null) {
			// log.setIp(uid);
		}
		assert log != null;
		log.setIp(ip);

		String loginPath = "login";
		if (loginPath.equals(signature.getName())) {
			try {
				assert argValues != null;
				username = new JSONObject((Integer) argValues[0]).get("username").toString();
			} catch (Exception e) {
				LogUtils.error(e);
			}
		}
		log.setLocation(StringUtils.getCityInfo(log.getIp()));
		log.setMethod(methodName);
		log.setUsername(username);
		log.setParams(params + " }");
		this.save(log);
	}

	@Override
	public Object findByErrDetail(Long id) {
		Log log = this.getById(id);
		// ValidationUtil.isNull(log.getId(), "Log", "id", id);
		String details = log.getExDetail();
		return Dict.create().set("exception", details);
	}

	@Override
	public void download(List<Log> logs, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Log log : logs) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("用户名", log.getUsername());
			map.put("IP", log.getIp());
			map.put("IP来源", log.getLocation());
			map.put("描述", log.getDescription());
			map.put("浏览器", log.getBrowser());
			map.put("请求耗时/毫秒", log.getConsumingTime());
			map.put("异常详情", log.getExDetail());
			//map.put("创建日期", log.getCreateTime());
			list.add(map);
		}
		// FileUtil.downloadExcel(list, response);
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

	@Override
	public long findIp(String toString, String toString1) {
		return 0;
	}
}
