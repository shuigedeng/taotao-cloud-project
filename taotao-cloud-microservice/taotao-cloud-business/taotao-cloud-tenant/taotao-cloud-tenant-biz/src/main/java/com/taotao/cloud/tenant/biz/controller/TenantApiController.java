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

package com.taotao.cloud.tenant.biz.controller;

import com.taotao.boot.web.annotation.InnerApi;
import com.taotao.cloud.tenant.api.feign.TenantServiceApi;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.biz.service.TenantService;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户表
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 10:58:13
 */
@InnerApi
@RestController
@RequestMapping
@AllArgsConstructor
public class TenantApiController implements TenantServiceApi {

    private final TenantService tenantService;
    private final ISeataTccService seataTccService;

    @Override
    public void validTenant(Long id) {

    }

    @Override
    public String addTenantWithTestSeata(TenantDTO tenantDTO) {
//            tenantService.addSysTenant(tenantDTO);

        seataTccService.tryInsert(tenantDTO, tenantDTO.getId());

        return "success";
    }
}
