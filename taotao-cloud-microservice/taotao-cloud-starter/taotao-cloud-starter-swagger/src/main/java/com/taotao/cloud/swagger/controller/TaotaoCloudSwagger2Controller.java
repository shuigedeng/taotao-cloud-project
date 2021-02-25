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
package com.taotao.cloud.swagger.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <br>
 *
 * @author dengtao
 * @since 2020/5/18 11:28
 * @version 1.0.0
 */
@Controller
public class TaotaoCloudSwagger2Controller implements InitializingBean {
    @Resource
    private Environment environment;
    @Resource
    private DocumentationCache documentationCache;
    @Resource
    private ServiceModelToSwagger2Mapper mapper;
    @Resource
    private JsonSerializer jsonSerializer;

    private Swagger2Controller swagger2Controller;

    @Override
    public void afterPropertiesSet() throws Exception {
        swagger2Controller = new Swagger2Controller(environment, documentationCache, mapper, jsonSerializer);
    }

    @RequestMapping(value = "/api-docs", method = RequestMethod.GET, produces = {"application/json", "application/hal+json"})
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(
            @RequestParam(value = "group", required = false) String swaggerGroup,
            HttpServletRequest servletRequest) {
        return swagger2Controller.getDocumentation(swaggerGroup, servletRequest);
    }

}
