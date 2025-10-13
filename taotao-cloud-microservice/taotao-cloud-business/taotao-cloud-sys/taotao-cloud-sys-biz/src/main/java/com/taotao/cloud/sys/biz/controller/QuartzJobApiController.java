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

package com.taotao.cloud.sys.biz.controller;

import com.taotao.boot.web.annotation.Api;
import com.taotao.cloud.sys.api.feign.QuartzJobApi;
import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.sys.biz.task.job.quartz.service.QuartzJobService;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 石英工作控制器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-09 15:18:19
 */
@Api
@AllArgsConstructor
@Validated
@RestController
@RequestMapping
public class QuartzJobApiController implements QuartzJobApi {

    private final QuartzJobService quartzJobService;
    private final ISeataTccService seataTccService;

    @Override
    public Boolean addQuartzJobDTOTestSeata(@RequestBody QuartzJobDTO quartzJobDTO) {
//        quartzJobService.addQuartzJobDTOTestSeata(quartzJobDTO);

        seataTccService.tryInsert(quartzJobDTO, quartzJobDTO.getId());

        return true;
    }

}
