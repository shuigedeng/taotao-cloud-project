package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignRoleService;
import com.taotao.cloud.sys.api.vo.role.RoleQueryVO;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignRoleFallbackImpl implements FallbackFactory<IFeignRoleService> {
    @Override
    public IFeignRoleService create(Throwable throwable) {
        return new IFeignRoleService() {
            @Override
            public Result<List<RoleQueryVO>> findRoleByUserId(Long userId) {
                LogUtil.error("调用findUserInfoByUsername异常：{}", throwable, userId);
                return Result.fail(null, 500);
            }
        };
    }
}
