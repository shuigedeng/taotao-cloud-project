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

package com.taotao.cloud.workflow.biz.common.flowable.feign.fallback;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.flowable.api.feign.IFeignChatbotService;
import com.taotao.cloud.flowable.api.model.vo.ChatbotVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignChatbotFallback implements FallbackFactory<IFeignChatbotService> {

    @Override
    public IFeignChatbotService create(Throwable throwable) {
        return new IFeignChatbotService() {

            @Override
            public Result<ChatbotVO> findChatbotById(Long id) {
                LogUtils.error("调用getMemberSecurityUser异常：{}", throwable, id);
                return Result.fail(null, 500);
            }
        };
    }
}
