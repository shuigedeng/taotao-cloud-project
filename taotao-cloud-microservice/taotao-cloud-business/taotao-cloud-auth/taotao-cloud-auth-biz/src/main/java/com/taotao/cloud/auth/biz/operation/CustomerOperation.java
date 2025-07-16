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

package com.taotao.cloud.auth.biz.operation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.stereotype.Component;

/**
 * GlobalOperationCustomizer:
 * 针对Operation级别的全局自定义扩展钩子函数，开发者可以对接口中每一个Operation进行扩展自定义实现，或调整，或修改，或增加扩展都行，
 * <p>
 * GlobalOpenApiCustomizer: 针对整个OpenAPI级别的,开发者在分组或者分包后，得到的单个OpenAPI实例，开发者可以操纵全局的OpenAPI实例，
 * 该OpenAPI对象已经是springdoc解析过的实例对象，例如该issues中的需求，开发者只需要自定义创建新Operation对象，然后通过OpenAPI实例对象进行add添加即可完成此需求
 *
 * @author shuigedeng
 * @version 2023.07
 * @see GlobalOpenApiCustomizer
 * @since 2023-07-14 08:36:51
 */
@Slf4j
@Component
public class CustomerOperation implements GlobalOpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        log.info("customer.");
        // 因为要新增自定义的接口，直接这里add
        PathItem pathItem = new PathItem();
        // 基础信息 构建Operation
        Operation operation = new Operation();
        operation.operationId("login");
        operation.summary("登录接口");
        operation.description("根据用户名和密码登录获取token");
        operation.tags(Collections.singletonList("登录"));
        // 构建参数
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(
                new Parameter()
                        .name("name")
                        .example("zhangFei")
                        .description("用户名")
                        .required(true)
                        .schema(new StringSchema())
                        .in("query"));
        parameters.add(
                new Parameter()
                        .name("password")
                        .example("123456")
                        .description("密码")
                        .required(true)
                        .schema(new StringSchema())
                        .in("query"));
        operation.parameters(parameters);

        // 构建响应body
        ApiResponses apiResponses = new ApiResponses();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse
                .description("ok")
                .content(
                        new Content()
                                .addMediaType("*/*", new MediaType().schema(new StringSchema())));
        apiResponses.addApiResponse("200", apiResponse);
        operation.responses(apiResponses);
        // 该自定义接口为post
        pathItem.post(operation);
        openApi.path("/login", pathItem);
    }
}
