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

package com.taotao.cloud.log.biz.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.biz.entity.SysLogLogin;
import com.taotao.cloud.log.biz.service.SysLogLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/sysLogLogin")
@Tag(name = "工具管理端-登录记录管理", description = "工具管理端-登录记录管理")
public class SysLogLoginController {

	private final SysLogLoginService sysLogLoginService;

	/**
	 * 分页查询
	 *
	 * @param page        分页对象
	 * @param sysLogLogin
	 * @return Response
	 */
	@GetMapping("/page")
	//@ApiOperation(value = "分页查询", notes = "分页查询")
	public Result<Page> page(Page page, SysLogLogin sysLogLogin) {
		page.setOrders(Arrays.asList(OrderItem.desc("login_time")));
		return Result.success(sysLogLoginService.page(page, Wrappers.query(sysLogLogin)));
	}


	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return Response
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	public Result<SysLogLogin> getById(@PathVariable Integer id) {
		return Result.success(sysLogLoginService.getById(id));
	}

	/**
	 * 新增
	 *
	 * @param sysLogLogin
	 * @return Response
	 */
	//@Anonymous
	//@SysLog("新增")
	@PostMapping
	//@ApiOperation(value = "新增", notes = "新增")
	public Result<Boolean> save(@RequestBody SysLogLogin sysLogLogin) {
		return Result.success(sysLogLoginService.save(sysLogLogin));
	}

	/**
	 * 修改
	 *
	 * @param sysLogLogin
	 * @return Response
	 */
	//@SysLog("编辑")
	@PutMapping
	//@ApiOperation(value = "修改", notes = "修改")
	@PreAuthorize("@authorize.hasPermission('sys_editor_sysLogLogin')")
	public Result<Boolean> update(@RequestBody SysLogLogin sysLogLogin) {
		return Result.success(sysLogLoginService.updateById(sysLogLogin));
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return Response
	 */
	//@SysLog("通过id删除")
	@DeleteMapping("/{id}")
	//@ApiOperation(value = "通过id删除", notes = "通过id删除")
	@PreAuthorize("@authorize.hasPermission('sys_delete_sysLogLogin')")
	public Result<Boolean> delete(@PathVariable Integer id) {
		return Result.success(sysLogLoginService.removeById(id));
	}

}
