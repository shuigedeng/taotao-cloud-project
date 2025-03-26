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

package com.taotao.cloud.message.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.boot.common.model.PageResult;
import com.taotao.cloud.message.api.feign.fallback.StoreMessageApiFallback;
import com.taotao.cloud.message.api.feign.response.StoreMessageApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceNameConstants.TAOTAO_CLOUD_MESSAGE, fallbackFactory = StoreMessageApiFallback.class)
public interface StoreMessageApi {

    @GetMapping(value = "/message/getPage")
    PageResult<StoreMessageApiResponse> getPage(PageQuery PageQuery);
}
