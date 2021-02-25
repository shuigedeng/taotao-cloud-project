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
package com.taotao.cloud.auth.biz.controller;

import com.taotao.cloud.auth.api.dto.ClientDTO;
import com.taotao.cloud.auth.api.query.ClientPageQuery;
import com.taotao.cloud.auth.biz.entity.Client;
import com.taotao.cloud.auth.biz.service.IClientService;
import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.core.utils.SecurityUtil;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用管理API
 *
 * @author dengtao
 * @since 2020/4/29 15:10
 * @version 1.0.0
 */
@Api(value = "应用管理API", tags = {"应用管理API"})
@Validated
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @ApiOperation(value = "应用列表")
    @RequestOperateLog(description = "应用列表")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @GetMapping("/page")
    public PageResult<Client> list(@Validated ClientPageQuery clientPageQuery) {
        Page<Client> clientPage = clientService.listClient(clientPageQuery);
        return PageResult.succeed(clientPage);
    }

    @ApiOperation(value = "根据id获取应用")
    @RequestOperateLog(description = "根据id获取应用")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @GetMapping("/{clientId}")
    public Result<Client> getByClientId(@PathVariable(value = "clientId") String clientId) {
        Client client = clientService.getByClientId(clientId);
        return Result.succeed(client);
    }

    @ApiOperation(value = "获取所有应用")
    @RequestOperateLog(description = "获取所有应用")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @GetMapping
    public Result<List<Client>> getAllClient() {
        SecurityUser user1 = SecurityUtil.getUser();
        List<Client> result = clientService.getAllClient();
        return Result.succeed(result);
    }

    @ApiOperation(value = "删除应用")
    @RequestOperateLog(description = "删除应用")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @DeleteMapping("/{clientId}")
    public Result<Boolean> delete(@PathVariable(value = "clientId") String clientId) {
        Boolean result = clientService.delByClientId(clientId);
        return Result.succeed(result);
    }

    @ApiOperation(value = "保存应用")
    @RequestOperateLog(description = "保存应用")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody ClientDTO clientDto) {
        Boolean result = clientService.saveClient(clientDto);
        return Result.succeed(result);
    }

    @ApiOperation(value = "修改应用")
    @RequestOperateLog(description = "修改应用")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @PutMapping("/{clientId}")
    public Result<Boolean> update(@PathVariable(value = "clientId") String clientId,
                                  @Validated @RequestBody ClientDTO clientDto) {
        Boolean result = clientService.updateClient(clientId, clientDto);
        return Result.succeed(result);
    }
}
