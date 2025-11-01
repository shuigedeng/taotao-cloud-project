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

package com.taotao.cloud.gateway.anti_reptile.handler;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.gateway.anti_reptile.ValidateFormService;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class RefreshFormHandler implements HandlerFunction<ServerResponse> {

    private final ValidateFormService validateFormService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        try {
            String result = validateFormService.refresh(request);

            return ServerResponse.status(HttpStatus.HTTP_OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Result.success(result));
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.HTTP_OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
        }
    }
}
