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

package com.taotao.cloud.payment.biz.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhuicloud.common.core.utils.Response;
import com.xhuicloud.common.log.annotation.SysLog;
import com.xhuicloud.pay.entity.PayChannel;
import com.xhuicloud.pay.service.PayChannelService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @program: XHuiCloud
 * @description: PayChannelController
 * @author: Sinda
 * @create: 2020-06-05 14:40
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/channel")
@Api(value = "channel", tags = "商户渠道管理")
public class PayChannelController {

    private final PayChannelService payChannelService;

    /**
     * 分页查询商户渠道列表
     *
     * @return
     */
    @GetMapping("/page")
    public Response<Page> page(Page page) {
        return Response.success(payChannelService.page(page));
    }

    /**
     * 添加商户渠道
     *
     * @return
     */
    @SysLog("添加商户渠道")
    @PostMapping
    @PreAuthorize("@authorize.hasPermission('sys_add_channel')")
    public Response<Boolean> save(@RequestBody PayChannel payChannel) {
        return Response.success(payChannelService.save(payChannel));
    }

    /**
     * 编辑商户渠道
     *
     * @param payChannel
     * @return
     */
    @SysLog("编辑商户渠道")
    @PutMapping
    @PreAuthorize("@authorize.hasPermission('sys_editor_user')")
    public Response<Boolean> update(@RequestBody PayChannel payChannel) {
        return Response.success(payChannelService.updateById(payChannel));
    }
}
