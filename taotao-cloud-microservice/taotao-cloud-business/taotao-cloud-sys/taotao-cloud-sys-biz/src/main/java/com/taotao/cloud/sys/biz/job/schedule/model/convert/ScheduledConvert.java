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

package com.taotao.cloud.sys.biz.job.schedule.model.convert;

import com.taotao.cloud.job.api.model.vo.ScheduledJobVO;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 将转换
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:13:39
 */
@Mapper
public interface ScheduledConvert {

	ScheduledConvert INSTANCE = Mappers.getMapper(ScheduledConvert.class);

	List<ScheduledJobVO> convertList(List<ScheduledJob> jobs);

}
