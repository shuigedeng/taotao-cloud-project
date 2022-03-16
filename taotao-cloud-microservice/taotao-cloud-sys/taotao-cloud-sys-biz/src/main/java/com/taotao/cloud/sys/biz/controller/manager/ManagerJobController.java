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
package com.taotao.cloud.sys.biz.controller.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.sys.api.dto.job.JobSaveDTO;
import com.taotao.cloud.sys.api.dto.job.JobUpdateDTO;
import com.taotao.cloud.sys.api.vo.job.JobQueryVO;
import com.taotao.cloud.sys.biz.entity.system.Job;
import com.taotao.cloud.sys.biz.service.IJobService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理端-岗位管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:01:47
 */
@Validated
@RestController
@RequestMapping("/sys/manager/job")
@Tag(name = "平台管理端-岗位管理API", description = "平台管理端-岗位管理API")
public class ManagerJobController extends
	SuperController<IJobService, Job, Long, BaseQuery, JobSaveDTO, JobUpdateDTO, JobQueryVO> {


}
