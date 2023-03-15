package com.taotao.cloud.sa.just.biz.just.justauth.service;

import com.gitegg.platform.base.result.Result;
import com.gitegg.service.extension.client.dto.JustAuthSocialInfoDTO;
import com.gitegg.service.extension.justauth.dto.JustAuthSocialDTO;
import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.gitegg.service.extension.justauth.entity.JustAuthSocialUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author GitEgg
 * @date 2022/6/18
 */
public interface IJustAuthService {

    /**
     * 查询第三方用户绑定关系
     */
    Long userBindId(@NotBlank @RequestParam("uuid") String uuid, @NotBlank @RequestParam("source") String source);

    /**
     * 创建或更新第三方用户信息
     */
    Long userCreateOrUpdate(@NotNull @RequestBody JustAuthSocialInfoDTO justAuthSocialInfoDTO);

    /**
     * 查询绑定第三方用户信息
     */
    Result<Object> userBindQuery(@NotNull @RequestParam("socialId") Long socialId);

    /**
     * 查询第三方用户信息
     * @param socialId
     * @return
     */
    JustAuthSocial querySocialInfo(@NotNull @RequestParam("socialId") Long socialId);

    /**
     * 绑定第三方用户信息
     */
    JustAuthSocialUser userBind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);

    /**
     * 解绑第三方用户信息"
     */
    Result<JustAuthSocialUser> userUnbind(@NotNull @RequestParam("socialId") Long socialId, @NotNull @RequestParam("userId") Long userId);
    
}
