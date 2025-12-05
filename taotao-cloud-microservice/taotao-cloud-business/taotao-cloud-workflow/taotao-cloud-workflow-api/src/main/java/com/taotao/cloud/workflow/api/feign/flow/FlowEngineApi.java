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

package com.taotao.cloud.workflow.api.feign.flow;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * api接口
 *
 * @author 
 * 
 * 
 * @since 2021/3/15 11:55
 */
// @HttpExchange(name = FeignName.WORKFLOW_SERVER_NAME , fallback = FlowEngineApiFallback.class, path
// = "/Engine/FlowEngine")
public interface FlowEngineApi {

//    @PostMapping(value = "/tableCre")
//    TableModels tableCre(@RequestBody TableCreModels tableCreModels) throws WorkFlowException;
//
//    @PostMapping(value = "/create")
//    void create(@RequestBody @Valid FlowEngineEntity flowEngineEntity) throws WorkFlowException;
//
//    @GetMapping(value = "/getInfoByID/{id}")
//    FlowEngineEntity getInfoByID(@PathVariable("id") String id);
//
//    @PostMapping(value = "/updateByID/{id}")
//    void updateByID(@PathVariable("id") String id, @RequestBody FlowEngineEntity flowEngineEntity)
//            throws WorkFlowException;
//
//    @PostMapping(value = "/getAppPageList")
//    FlowAppPageModel getAppPageList(@RequestBody FlowPagination pagination);
//
//    @PostMapping(value = "/getFlowList")
//    List<FlowEngineEntity> getFlowList(@RequestBody List<String> id);
}
