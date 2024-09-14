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

package com.taotao.cloud.sys.api.feign;

import com.taotao.boot.common.constant.ServiceName;
import com.taotao.cloud.sys.api.feign.fallback.QuartzJobApiFallback;
import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_JOB, fallbackFactory = QuartzJobApiFallback.class)
public interface QuartzJobApi {

    @PostMapping("/job/addQuartzJobDTOTestSeata")
    public Boolean addQuartzJobDTOTestSeata(@Validated @RequestBody QuartzJobDTO quartzJobDTO);
}
