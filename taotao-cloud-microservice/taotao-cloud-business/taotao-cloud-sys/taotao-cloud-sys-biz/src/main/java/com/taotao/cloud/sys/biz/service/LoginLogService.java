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

package com.taotao.cloud.sys.biz.service;

import com.taotao.boot.common.model.PageResult;
import com.taotao.cloud.sys.biz.model.dto.LoginLogDto;
import com.taotao.cloud.sys.biz.model.param.LoginLogParam;

/**
 * 登陆日志
 *
 * @author shuigedeng
 * @since 2021/12/2
 */
public interface LoginLogService {

	/**
	 * 添加
	 */
	void add(LoginLogParam loginLog);

	/**
	 * 获取
	 */
	LoginLogDto findById(Long id);

	/**
	 * 分页
	 */
	PageResult<LoginLogDto> page(LoginLogParam loginLogParam);

	/**
	 * 删除
	 */
	void delete(Long id);
}
