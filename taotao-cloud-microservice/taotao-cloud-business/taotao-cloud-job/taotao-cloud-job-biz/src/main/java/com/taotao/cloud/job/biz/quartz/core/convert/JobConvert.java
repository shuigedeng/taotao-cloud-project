/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.biz.quartz.core.convert;

import com.art.scheduled.core.dto.JobDTO;
import com.art.scheduled.dao.dataobject.JobDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/12/7 21:43
 */
@Mapper
public interface JobConvert {

	JobConvert INSTANCE = Mappers.getMapper(JobConvert.class);

	JobDO convert(JobDTO dto);

	JobDTO convert(JobDO jobDO);

	Page<JobDTO> convertPage(Page<JobDO> page);

}
