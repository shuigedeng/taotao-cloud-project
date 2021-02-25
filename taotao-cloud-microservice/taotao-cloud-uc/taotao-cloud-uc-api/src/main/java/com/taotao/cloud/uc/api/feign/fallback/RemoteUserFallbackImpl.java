package com.taotao.cloud.uc.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.uc.api.feign.RemoteUserService;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @date 2020/4/29 21:43
 */
public class RemoteUserFallbackImpl implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable throwable) {
        return new RemoteUserService() {
            @Override
            public Result<UserVO> findUserInfoByUsername(String username) {
                LogUtil.error("调用findUserInfoByUsername异常：{}", throwable, username);
                return Result.failed(null, 500);
            }

            @Override
            public Result<SecurityUser> getUserInfoBySocial(String providerId, int providerUserId) {
                LogUtil.error("调用getUserInfoBySocial异常：providerId: {}, providerUserId: {}", throwable, providerId, providerUserId);
                return Result.failed(null, 500);
            }
        };
    }
}
