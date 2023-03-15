package com.taotao.cloud.sa.just.biz.just.justauth.service.impl;

import com.gitegg.service.extension.client.dto.JustAuthSocialInfoDTO;
import com.gitegg.service.extension.justauth.dto.*;
import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import com.gitegg.service.extension.justauth.service.IJustAuthService;
import com.gitegg.service.extension.justauth.service.IJustAuthSocialService;
import com.gitegg.service.extension.justauth.service.IJustAuthSocialUserService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: JustAuthFeign
 * @Description: JustAuthFeign前端控制器
 * @author gitegg
 * @date 2019年5月18日 下午4:03:58
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthServiceImpl implements IJustAuthService {

    private final IJustAuthSocialService justAuthSocialService;
    
    private final IJustAuthSocialUserService justAuthSocialUserService;

    /**
     * 查询第三方用户绑定关系
     */
    @Override
    public Long userBindId(@NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source) {
        QueryJustAuthSocialDTO queryJustAuthSocialDTO = new QueryJustAuthSocialDTO();
        queryJustAuthSocialDTO.setUuid(uuid);
        queryJustAuthSocialDTO.setSource(source);
        Long userId = justAuthSocialService.queryUserIdBySocial(queryJustAuthSocialDTO);
        return userId;
    }

    /**
     * 创建或更新第三方用户信息
     */
    @Override
    public Long userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO) {
        UpdateJustAuthSocialDTO createJustAuthSocialDTO = BeanCopierUtils.copyByClass(justAuthSocialInfoDTO, UpdateJustAuthSocialDTO.class);
        JustAuthSocial justAuthSocial = justAuthSocialService.createOrUpdateJustAuthSocial(createJustAuthSocialDTO);
        return justAuthSocial.getId();
    }

    /**
     * 查询绑定第三方用户信息
     */
    @Override
    public Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId) {
        QueryJustAuthSocialUserDTO justAuthSocialUserQuery = new QueryJustAuthSocialUserDTO();
        justAuthSocialUserQuery.setSocialId(socialId);
        List<JustAuthSocialUserDTO> justAuthSocialUserList = justAuthSocialUserService.queryJustAuthSocialUserList(justAuthSocialUserQuery);
        if (CollectionUtils.isEmpty(justAuthSocialUserList))
        {
            return Result.error(ResultCodeEnum.BIND_NOT_FOUND);
        }
        else if (!CollectionUtils.isEmpty(justAuthSocialUserList) && justAuthSocialUserList.size() > 1)
        {
            return Result.error(ResultCodeEnum.BIND_MULTIPLE);
        }
        return Result.data(justAuthSocialUserList.get(GitEggConstant.Number.ZERO).getUserId());
    }
    
    /**
     * 查询第三方用户信息
     * @param socialId
     * @return
     */
    @Override
    public JustAuthSocial querySocialInfo(@NotNull @RequestParam("socialId") Long socialId)
    {
        JustAuthSocial justAuthSocial = justAuthSocialService.getById(socialId);
        return justAuthSocial;
    }

    /**
     * 绑定第三方用户信息
     */
    @Override
    public JustAuthSocialUser userBind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        CreateJustAuthSocialUserDTO justAuthSocialUserCreate = new CreateJustAuthSocialUserDTO();
        justAuthSocialUserCreate.setSocialId(socialId);
        justAuthSocialUserCreate.setUserId(userId);
        JustAuthSocialUser justAuthSocialUser = justAuthSocialUserService.createJustAuthSocialUser(justAuthSocialUserCreate);
        return justAuthSocialUser;
    }

    /**
     * 解绑第三方用户信息"
     */
    @Override
    public Result<JustAuthSocialUser> userUnbind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId) {
        QueryJustAuthSocialUserDTO justAuthSocialUserQuery = new QueryJustAuthSocialUserDTO();
        justAuthSocialUserQuery.setSocialId(socialId);
        justAuthSocialUserQuery.setUserId(userId);
        JustAuthSocialUserDTO justAuthSocialUserDTO = justAuthSocialUserService.queryJustAuthSocialUser(justAuthSocialUserQuery);
        if (null == justAuthSocialUserDTO)
        {
            return Result.error(ResultCodeEnum.BIND_NOT_FOUND);
        }
        justAuthSocialUserService.deleteJustAuthSocialUser(justAuthSocialUserDTO.getId());
        return Result.success();
    }
}
