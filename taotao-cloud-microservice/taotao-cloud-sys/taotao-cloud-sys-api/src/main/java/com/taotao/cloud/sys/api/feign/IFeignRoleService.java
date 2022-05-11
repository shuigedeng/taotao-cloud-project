package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignRoleFallback;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallback;
import com.taotao.cloud.sys.api.vo.role.RoleQueryVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台角色模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:31
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignRoleFallback.class)
public interface IFeignRoleService {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     * @since 2020/10/21 15:13
     */
    @GetMapping("/role/info/userId")
    Result<List<RoleQueryVO>> findRoleByUserId(@RequestParam(value = "userId") Long userId);
}

