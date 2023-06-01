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

package com.taotao.cloud.wechat.biz.mp.controller.admin.user;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserPageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.yudao.module.mp.convert.user.MpUserConvert;
import cn.iocoder.yudao.module.mp.dal.dataobject.user.MpUserDO;
import cn.iocoder.yudao.module.mp.service.user.MpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理后台 - 公众号粉丝")
@RestController
@RequestMapping("/mp/user")
@Validated
public class MpUserController {

    @Resource
    private MpUserService mpUserService;

    @GetMapping("/page")
    @ApiOperation("获得公众号粉丝分页")
    @PreAuthorize("@ss.hasPermission('mp:user:query')")
    public CommonResult<PageResult<MpUserRespVO>> getUserPage(@Valid MpUserPageReqVO pageVO) {
        PageResult<MpUserDO> pageResult = mpUserService.getUserPage(pageVO);
        return success(MpUserConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/get")
    @ApiOperation("获得公众号粉丝")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('mp:user:query')")
    public CommonResult<MpUserRespVO> getUser(@RequestParam("id") Long id) {
        return success(MpUserConvert.INSTANCE.convert(mpUserService.getUser(id)));
    }

    @PutMapping("/update")
    @ApiOperation("更新公众号粉丝")
    @PreAuthorize("@ss.hasPermission('mp:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody MpUserUpdateReqVO updateReqVO) {
        mpUserService.updateUser(updateReqVO);
        return success(true);
    }

    @PostMapping("/sync")
    @ApiOperation("同步公众号粉丝")
    @ApiImplicitParam(name = "accountId", value = "公众号账号的编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('mp:user:sync')")
    public CommonResult<Boolean> syncUser(@RequestParam("accountId") Long accountId) {
        mpUserService.syncUser(accountId);
        return success(true);
    }
}
