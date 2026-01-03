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

package com.taotao.cloud.tx.rm.interceptor;

import com.taotao.cloud.tx.rm.transactional.TtcTxParticipant;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//  Spring框架预留的钩子接口：织入事务组ID
/**
 * GroupIdRespAdvice
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@ControllerAdvice
public class GroupIdRespAdvice implements ResponseBodyAdvice {

    // 钩子类的前置方法：必须为true才会执行beforeBodyWrite()方法
    @Override
    public boolean supports( MethodParameter methodParameter, Class aClass ) {
        return true;
    }

    // Controller方法执行完成之后，响应报文组装之前执行
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class aClass,
            ServerHttpRequest request,
            ServerHttpResponse response ) {
        // 如果ThreadLocal中的事务组ID不为空，代表当前请求参与了分布式事务，
        // 会获取对应的事务组ID放入到响应头中（对于普通请求不会改写响应头）
        if (TtcTxParticipant.getCurrentGroupId() != null) {
            // 把需要传递的事务组ID、子事务数量放入响应头中
            response.getHeaders().set("groupId", TtcTxParticipant.getCurrentGroupId());
            response.getHeaders()
                    .set(
                            "transactionalCount",
                            String.valueOf(TtcTxParticipant.getTransactionCount()));
        }
        return body;
    }
}
