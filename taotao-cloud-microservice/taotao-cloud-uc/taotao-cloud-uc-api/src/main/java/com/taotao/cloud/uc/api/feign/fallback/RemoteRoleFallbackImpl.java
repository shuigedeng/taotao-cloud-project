package com.taotao.cloud.uc.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.uc.api.feign.RemoteRoleService;
import com.taotao.cloud.uc.api.vo.role.RoleVO;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteRoleFallbackImpl implements FallbackFactory<RemoteRoleService> {
    @Override
    public RemoteRoleService create(Throwable throwable) {
        return new RemoteRoleService() {
            @Override
            public Result<List<RoleVO>> findRoleByUserId(Long userId) {
                LogUtil.error("调用findUserInfoByUsername异常：{}", throwable, userId);
                return Result.fail(null, 500);
            }
        };
    }
}
