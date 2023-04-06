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

package com.taotao.cloud.payment.biz.bootx.controller;

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service.AlipayConfigService;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.alipay.AlipayConfigDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.alipay.AlipayConfigParam;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.alipay.AlipayConfigQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @date 2021/2/26
 */
@Tag(name = "支付宝配置")
@RestController
@RequestMapping("/alipay")
@AllArgsConstructor
public class AlipayConfigController {
    private final AlipayConfigService alipayConfigService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<AlipayConfigDto> add(@RequestBody AlipayConfigParam param) {
        return Res.ok(alipayConfigService.add(param));
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<AlipayConfigDto> update(@RequestBody AlipayConfigParam param) {
        return Res.ok(alipayConfigService.update(param));
    }

    @Operation(summary = "启用指定的支付宝配置")
    @PostMapping("/setUpActivity")
    public ResResult<Void> setUpActivity(Long id) {
        alipayConfigService.setUpActivity(id);
        return Res.ok();
    }

    @Operation(summary = "清除指定的支付宝配置")
    @PostMapping("/clearActivity")
    public ResResult<Void> clearActivity(Long id) {
        alipayConfigService.clearActivity(id);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AlipayConfigDto>> page(PageQuery PageQuery, AlipayConfigQuery param) {
        return Res.ok(alipayConfigService.page(PageQuery, param));
    }

    @Operation(summary = "根据Id查询")
    @GetMapping("/findById")
    public ResResult<AlipayConfigDto> findById(Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWayList")
    public ResResult<List<KeyValue>> findPayWayList() {
        return Res.ok(alipayConfigService.findPayWayList());
    }
}
