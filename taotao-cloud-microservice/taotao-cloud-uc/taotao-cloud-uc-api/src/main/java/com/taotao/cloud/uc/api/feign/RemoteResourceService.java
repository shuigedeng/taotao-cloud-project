package com.taotao.cloud.uc.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.uc.api.feign.fallback.RemoteUserFallbackImpl;
import com.taotao.cloud.uc.api.vo.resource.ResourceVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 远程调用后台资源模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteResourceService", value = ServiceNameConstant.TAOTAO_CLOUD_UC_CENTER, fallbackFactory = RemoteUserFallbackImpl.class)
public interface RemoteResourceService {
    /**
     * 根据角色code列表获取角色列表
     *
     * @param codes
     * @return com.taotao.cloud.core.model.Result<java.util.List < com.taotao.cloud.uc.api.vo.resource.ResourceVO>>
     * @author dengtao
     * @date 2020/10/21 15:24
     * @since v1.0
     */
    @GetMapping("/resource/info/codes")
    Result<List<ResourceVO>> findResourceByCodes(@RequestParam(value = "codes") Set<String> codes);

}
