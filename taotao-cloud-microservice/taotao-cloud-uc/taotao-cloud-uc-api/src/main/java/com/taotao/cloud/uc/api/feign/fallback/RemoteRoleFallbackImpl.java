package com.taotao.cloud.uc.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.uc.api.feign.RemoteRoleService;
import com.taotao.cloud.uc.api.vo.role.RoleVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * RemoteLogFallbackImpl
 *
 * @author dengtao
 * @date 2020/4/29 21:43
 */
public class RemoteRoleFallbackImpl implements FallbackFactory<RemoteRoleService> {
    @Override
    public RemoteRoleService create(Throwable throwable) {
        return new RemoteRoleService() {
            @Override
            public Result<List<RoleVO>> findRoleByUserId(Long userId) {
                LogUtil.error("调用findUserInfoByUsername异常：{}", throwable, userId);
                return Result.failed(null, 500);
            }
        };
    }
}
