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

package com.xhuicloud.logs.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhuicloud.common.authorization.resource.annotation.Anonymous;
import com.xhuicloud.common.core.utils.Response;
import com.xhuicloud.common.log.annotation.SysLog;
import com.xhuicloud.logs.entity.SysLogLogin;
import com.xhuicloud.logs.service.SysLogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
* @program: logs
* @description:
* @author: Sinda
* @create: 2022-03-19 20:42:34
*/
@RestController
@AllArgsConstructor
@RequestMapping("/sysLogLogin" )
@Api(value = "sysLogLogin", tags = "登录记录管理")
public class SysLogLoginController {

    private final SysLogLoginService sysLogLoginService;

    /**
    * 分页查询
    *
    * @param page 分页对象
    * @param sysLogLogin
    * @return Response
    */
    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public Response<Page> page(Page page, SysLogLogin sysLogLogin) {
        page.setOrders(Arrays.asList(OrderItem.desc("login_time")));
        return Response.success(sysLogLoginService.page(page, Wrappers.query(sysLogLogin)));
    }


    /**
    * 通过id查询
    * @param id
    * @return Response
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    public Response<SysLogLogin> getById(@PathVariable Integer id) {
        return Response.success(sysLogLoginService.getById(id));
    }

    /**
    * 新增
    *
    * @param sysLogLogin
    * @return Response
    */
    @Anonymous
    @SysLog("新增" )
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public Response<Boolean> save(@RequestBody SysLogLogin sysLogLogin) {
        return Response.success(sysLogLoginService.save(sysLogLogin));
    }

    /**
    * 修改
    *
    * @param sysLogLogin
    * @return Response
    */
    @SysLog("编辑" )
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    @PreAuthorize("@authorize.hasPermission('sys_editor_sysLogLogin')" )
    public Response<Boolean> update(@RequestBody SysLogLogin sysLogLogin) {
        return Response.success(sysLogLoginService.updateById(sysLogLogin));
    }

    /**
    * 通过id删除
    *
    * @param id
    * @return Response
    */
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @PreAuthorize("@authorize.hasPermission('sys_delete_sysLogLogin')" )
    public Response<Boolean> delete(@PathVariable Integer id) {
        return Response.success(sysLogLoginService.removeById(id));
    }

}
