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

package com.taotao.cloud.workflow.biz.flowable.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.message.api.feign.IFeignStoreMessageService;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.api.vo.StoreMessageVO;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 管理端,店铺消息消息管理接口 */
@Validated
@RestController
@Tag(name = "管理端-店铺消息消息管理接口", description = "管理端-店铺消息消息管理接口")
@RequestMapping("/manager/message/store")
public class StoreMessageManagerController {

    @Autowired
    private IFeignStoreMessageService storeMessageService;

    @Operation(summary = "多条件分页获取", description = "多条件分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<PageResult<StoreMessageVO>> getByCondition(
            StoreMessageQueryVO storeMessageQueryVO, PageQuery PageQuery) {
        IPage<StoreMessageVO> page = storeMessageService.getPage(storeMessageQueryVO, PageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, StoreMessageVO.class));
    }
}
