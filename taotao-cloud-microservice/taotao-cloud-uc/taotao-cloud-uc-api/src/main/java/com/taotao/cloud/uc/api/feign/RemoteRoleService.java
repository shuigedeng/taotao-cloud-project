package com.taotao.cloud.uc.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.uc.api.feign.fallback.RemoteUserFallbackImpl;
import com.taotao.cloud.uc.api.vo.role.RoleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用后台角色模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteRoleService", value = ServiceNameConstant.TAOTAO_CLOUD_UC_CENTER, fallbackFactory = RemoteUserFallbackImpl.class)
public interface RemoteRoleService {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return com.taotao.cloud.core.model.Result<java.util.List < com.taotao.cloud.uc.api.vo.role.RoleVO>>
     * @author dengtao
     * @date 2020/10/21 15:13
     * @since v1.0
     */
    @GetMapping("/role/info/userId")
    Result<List<RoleVO>> findRoleByUserId(@RequestParam(value = "userId") Long userId);
}

