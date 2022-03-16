/*
 * Copyright 2002-2021 the original author or authors.
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

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.sys.biz.entity.config.LogisticsConfig;
import com.taotao.cloud.sys.biz.mapper.ILogisticsMapper;
import com.taotao.cloud.sys.biz.repository.cls.LogisticsRepository;
import com.taotao.cloud.sys.biz.repository.inf.ILogisticsRepository;
import com.taotao.cloud.sys.biz.service.ILogisticsService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/13 10:00
 */
@Service
public class LogisticsServiceImpl extends
	BaseSuperServiceImpl<ILogisticsMapper, LogisticsConfig, LogisticsRepository, ILogisticsRepository, Long>
	implements ILogisticsService {

	@Override
	public LogisticsConfig findLogisticsById(Long id) {
		Optional<LogisticsConfig> optionalExpressCompany = ir().findById(id);
		return optionalExpressCompany.orElseThrow(
			() -> new BusinessException(ResultEnum.FILE_NOT_EXIST));
	}
}
