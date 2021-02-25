package com.taotao.cloud.uc.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.uc.api.feign.fallback.RemoteUserFallbackImpl;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstant.TAOTAO_CLOUD_UC_CENTER, fallbackFactory = RemoteUserFallbackImpl.class)
public interface RemoteUserService {

    /**
     * 获取用户信息
     *
     * @param username 用户名称
     * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.uc.api.vo.user.UserVO>
     * @author dengtao
     * @date 2020/10/21 15:06
     * @since v1.0
     */
    @GetMapping(value = "/user/info/username")
    Result<UserVO> findUserInfoByUsername(@RequestParam(value = "username") String username);

    /**
     * 通过第三方查询用户包括角色权限等
     *
     * @param providerId     providerId
     * @param providerUserId providerUserId
     * @return com.taotao.cloud.common.model.Result<com.taotao.cloud.uc.api.dto.UserDetailsInfo>
     * @author dengtao
     * @date 2020/4/29 17:47
     */
    @GetMapping(value = "/user/info/social/{social}", headers = {"from=in"})
    Result<SecurityUser> getUserInfoBySocial(@RequestParam("providerId") String providerId,
                                             @RequestParam("providerUserId") int providerUserId);

}

