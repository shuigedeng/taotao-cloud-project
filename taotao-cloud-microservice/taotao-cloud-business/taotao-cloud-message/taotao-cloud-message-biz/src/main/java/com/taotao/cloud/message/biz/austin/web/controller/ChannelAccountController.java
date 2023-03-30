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

package com.taotao.cloud.message.biz.austin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.taotao.cloud.message.biz.austin.web.exception.CommonException;
import com.taotao.cloud.message.biz.austin.web.service.ChannelAccountService;
import com.taotao.cloud.message.biz.austin.web.utils.Convert4Amis;
import com.taotao.cloud.message.biz.austin.web.utils.LoginUtils;
import com.taotao.cloud.message.biz.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 渠道账号管理接口
 *
 * @author 3y
 */
@Slf4j
@AustinResult
@RestController
@RequestMapping("/account")
@Api("渠道账号管理接口")
public class ChannelAccountController {

    @Autowired private ChannelAccountService channelAccountService;

    @Autowired private LoginUtils loginUtils;

    /** 如果Id存在，则修改 如果Id不存在，则保存 */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public ChannelAccount saveOrUpdate(@RequestBody ChannelAccount channelAccount) {
        if (loginUtils.needLogin() && StrUtil.isBlank(channelAccount.getCreator())) {
            throw new CommonException(RespStatusEnum.NO_LOGIN);
        }
        channelAccount.setCreator(
                StrUtil.isBlank(channelAccount.getCreator())
                        ? AustinConstant.DEFAULT_CREATOR
                        : channelAccount.getCreator());

        return channelAccountService.save(channelAccount);
    }

    /** 根据渠道标识查询渠道账号相关的信息 */
    @GetMapping("/queryByChannelType")
    @ApiOperation("/根据渠道标识查询相关的记录")
    public List<CommonAmisVo> query(Integer channelType, String creator) {
        if (loginUtils.needLogin() && StrUtil.isBlank(creator)) {
            throw new CommonException(RespStatusEnum.NO_LOGIN);
        }
        creator = StrUtil.isBlank(creator) ? AustinConstant.DEFAULT_CREATOR : creator;

        List<ChannelAccount> channelAccounts =
                channelAccountService.queryByChannelType(channelType, creator);
        return Convert4Amis.getChannelAccountVo(channelAccounts, channelType);
    }

    /** 所有的渠道账号信息 */
    @GetMapping("/list")
    @ApiOperation("/渠道账号列表信息")
    public List<ChannelAccount> list(String creator) {
        if (loginUtils.needLogin() && StrUtil.isBlank(creator)) {
            throw new CommonException(RespStatusEnum.NO_LOGIN);
        }
        creator = StrUtil.isBlank(creator) ? AustinConstant.DEFAULT_CREATOR : creator;

        return channelAccountService.list(creator);
    }

    /** 根据Id删除 id多个用逗号分隔开 */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList =
                    Arrays.stream(id.split(StrUtil.COMMA))
                            .map(Long::valueOf)
                            .collect(Collectors.toList());
            channelAccountService.deleteByIds(idList);
        }
    }
}
