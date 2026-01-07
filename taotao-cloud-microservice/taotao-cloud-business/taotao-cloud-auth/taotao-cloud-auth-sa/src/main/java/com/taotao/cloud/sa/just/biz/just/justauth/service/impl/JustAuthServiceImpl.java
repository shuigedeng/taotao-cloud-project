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

package com.taotao.cloud.sa.just.biz.just.justauth.service.impl;

import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthService;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthSocialService;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthSocialUserService;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: JustAuthFeign @JustAuthFeign前端控制器
 *
 * @since 2019年5月18日 下午4:03:58
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthServiceImpl implements JustAuthService {

    private final JustAuthSocialService justAuthSocialService;

    private final JustAuthSocialUserService justAuthSocialUserService;

    /** 查询第三方用户绑定关系 */
    @Override
    public Long userBindId(
            @NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source) {
        QueryJustAuthSocialDTO queryJustAuthSocialDTO = new QueryJustAuthSocialDTO();
        queryJustAuthSocialDTO.setUuid(uuid);
        queryJustAuthSocialDTO.setSource(source);
        Long userId = justAuthSocialService.queryUserIdBySocial(queryJustAuthSocialDTO);
        return userId;
    }

    /** 创建或更新第三方用户信息 */
    @Override
    public Long userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO) {
        UpdateJustAuthSocialDTO createJustAuthSocialDTO =
                BeanCopierUtils.copyByClass(justAuthSocialInfoDTO, UpdateJustAuthSocialDTO.class);
        JustAuthSocial justAuthSocial = justAuthSocialService.createOrUpdateJustAuthSocial(createJustAuthSocialDTO);
        return justAuthSocial.getId();
    }

    /** 查询绑定第三方用户信息 */
    @Override
    public Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId) {
        QueryJustAuthSocialUserDTO justAuthSocialUserQuery = new QueryJustAuthSocialUserDTO();
        justAuthSocialUserQuery.setSocialId(socialId);
        List<JustAuthSocialUserDTO> justAuthSocialUserList =
                justAuthSocialUserService.queryJustAuthSocialUserList(justAuthSocialUserQuery);
        if (CollectionUtils.isEmpty(justAuthSocialUserList)) {
            return Result.error(ResultCodeEnum.BIND_NOT_FOUND);
        } else if (!CollectionUtils.isEmpty(justAuthSocialUserList) && justAuthSocialUserList.size() > 1) {
            return Result.error(ResultCodeEnum.BIND_MULTIPLE);
        }
        return Result.data(
                justAuthSocialUserList.get(GitEggConstant.Number.ZERO).getUserId());
    }

    /**
     * 查询第三方用户信息
     *
     * @param socialId
     * @return
     */
    @Override
    public JustAuthSocial querySocialInfo(@NotNull @RequestParam("socialId") Long socialId) {
        JustAuthSocial justAuthSocial = justAuthSocialService.getById(socialId);
        return justAuthSocial;
    }

    /** 绑定第三方用户信息 */
    @Override
    public JustAuthSocialUser userBind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        CreateJustAuthSocialUserDTO justAuthSocialUserCreate = new CreateJustAuthSocialUserDTO();
        justAuthSocialUserCreate.setSocialId(socialId);
        justAuthSocialUserCreate.setUserId(userId);
        JustAuthSocialUser justAuthSocialUser =
                justAuthSocialUserService.createJustAuthSocialUser(justAuthSocialUserCreate);
        return justAuthSocialUser;
    }

    /** 解绑第三方用户信息" */
    @Override
    public Result<JustAuthSocialUser> userUnbind(
            @NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        QueryJustAuthSocialUserDTO justAuthSocialUserQuery = new QueryJustAuthSocialUserDTO();
        justAuthSocialUserQuery.setSocialId(socialId);
        justAuthSocialUserQuery.setUserId(userId);
        JustAuthSocialUserDTO justAuthSocialUserDTO =
                justAuthSocialUserService.queryJustAuthSocialUser(justAuthSocialUserQuery);
        if (null == justAuthSocialUserDTO) {
            return Result.error(ResultCodeEnum.BIND_NOT_FOUND);
        }
        justAuthSocialUserService.deleteJustAuthSocialUser(justAuthSocialUserDTO.getId());
        return Result.success();
    }
}
