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

package com.taotao.cloud.wechat.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.cloud.wechat.api.feign.fallback.FeignDictApiFallback;
import com.taotao.cloud.wechat.api.feign.response.FeignDictResponse;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@HttpExchange(
        name = ServiceNameConstants.TAOTAO_CLOUD_SYS)
public interface WechatApi {

    /**
     * 字典列表code查询
     *
     * @param code 代码
     * @return {@link FeignDictResponse }
     * @since 2022-06-29 21:40:21
     */
    @GetMapping("/sys/remote/dict/code")
    FeignDictResponse findByCode(@RequestParam(value = "code") String code);
}
