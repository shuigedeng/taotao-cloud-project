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

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogDto;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogQueryCriteria;
import com.taotao.cloud.sys.biz.entity.quartz.QuartzLog;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

public interface IQuartzLogService extends IService<QuartzLog> {

	/**
	 * 查询数据分页
	 *
	 * @param criteria 条件
	 * @param pageable 分页参数
	 * @return Map<String, Object>
	 */
	Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 *
	 * @param criteria 条件参数
	 * @return List<QuartzLogDto>
	 */
	List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria);

	/**
	 * 导出数据
	 *
	 * @param all      待导出的数据
	 * @param response /
	 * @throws IOException /
	 */
	void download(List<QuartzLogDto> all, HttpServletResponse response) throws IOException;
}
