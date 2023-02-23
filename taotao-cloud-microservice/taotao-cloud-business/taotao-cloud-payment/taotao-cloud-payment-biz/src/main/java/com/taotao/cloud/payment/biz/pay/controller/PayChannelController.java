/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhuicloud.common.log.annotation.SysLog;
import com.xhuicloud.pay.entity.PayChannel;
import com.xhuicloud.pay.service.PayChannelService;
import com.xhuicloud.common.core.utils.Response;
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
