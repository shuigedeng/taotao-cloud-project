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

package com.taotao.cloud.sys.biz.supports.core.db.service;

import com.taotao.boot.common.model.PageResult;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.cloud.sys.biz.model.dto.LoginLogDto;
import com.taotao.cloud.sys.biz.model.param.LoginLogParam;
import com.taotao.cloud.sys.biz.service.LoginLogService;
import com.taotao.cloud.sys.biz.supports.core.db.dao.LoginLogDbManager;
import com.taotao.cloud.sys.biz.supports.core.db.entity.LoginLogDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 登陆日志
 *
 * @author shuigedeng
 * @since 2021/8/12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "log.store", value = "type", havingValue = "jdbc", matchIfMissing = true)
@RequiredArgsConstructor
public class LoginLogDbService implements LoginLogService {

	private final LoginLogDbManager loginLogManager;

	/**
	 * 添加
	 */
	@Override
	public void add(LoginLogParam loginLog) {
//		loginLogManager.save(LogConvert.CONVERT.convert(loginLog));
	}

	/**
	 * 获取
	 */
	@Override
	public LoginLogDto findById(Long id) {
//		return loginLogManager.findById(id).map(LoginLogDb::toDto).orElseThrow(RuntimeException::new);
		return null;
	}

	/**
	 * 分页
	 */
	@Override
	public PageResult<LoginLogDto> page(LoginLogParam loginLogParam) {
//		return MpUtils.convertMybatisPage(loginLogManager.page(loginLogParam), LoginLogDto.class);
		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public void delete(Long id) {
//		loginLogManager.deleteById(id);
	}
}
