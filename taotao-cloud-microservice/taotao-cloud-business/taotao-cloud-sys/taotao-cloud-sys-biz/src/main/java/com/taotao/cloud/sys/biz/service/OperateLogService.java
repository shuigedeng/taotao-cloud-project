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
import com.taotao.cloud.sys.biz.model.dto.OperateLogDto;
import com.taotao.cloud.sys.biz.model.param.OperateLogParam;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @since 2021/8/12
 */
public interface OperateLogService {

	/**
	 * 添加
	 */
	void add(OperateLogParam operateLog);

	/**
	 * 获取
	 */
	OperateLogDto findById(Long id);

	/**
	 * 分页
	 */
	PageResult<OperateLogDto> page(OperateLogParam operateLogParam);

	/**
	 * 删除
	 */
	void delete(Long id);
}
