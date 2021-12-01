package com.taotao.cloud.uc.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.uc.api.feign.fallback.FeignUserFallbackImpl;
import com.taotao.cloud.uc.api.vo.role.RoleQueryVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台角色模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteRoleService", value = ServiceName.TAOTAO_CLOUD_UC_CENTER, fallbackFactory = FeignUserFallbackImpl.class)
public interface IFeignRoleService {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return com.taotao.cloud.core.model.Result<java.util.List < com.taotao.cloud.uc.api.vo.role.RoleVO>>
     * @author shuigedeng
     * @since 2020/10/21 15:13
     * @version 1.0.0
     */
    @GetMapping("/role/info/userId")
    Result<List<RoleQueryVO>> findRoleByUserId(@RequestParam(value = "userId") Long userId);
}

