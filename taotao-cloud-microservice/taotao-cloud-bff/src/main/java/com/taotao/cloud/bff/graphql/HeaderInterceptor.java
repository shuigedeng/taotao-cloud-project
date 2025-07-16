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

package com.taotao.cloud.bff.graphql;

import java.util.Collections;
import java.util.List;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

/**
 * @Controller class MyController {
 * @QueryMapping Person person(@ContextValue String myHeader) { // ... } }
 */
public class HeaderInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        List<String> headerValue = request.getHeaders().get("myHeader");
        request.configureExecutionInput(
                (executionInput, builder) ->
                        builder.graphQLContext(Collections.singletonMap("myHeader", headerValue))
                                .build());
        return chain.next(request);
    }

    public static class MyInterceptor implements WebGraphQlInterceptor {

        @Override
        public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
            return chain.next(request)
                    .map(
                            response -> {
                                Object data = response.getData();
                                Object updatedData = "sldfj";
                                return response.transform(builder -> builder.data(updatedData));
                            });
        }
    }
}
