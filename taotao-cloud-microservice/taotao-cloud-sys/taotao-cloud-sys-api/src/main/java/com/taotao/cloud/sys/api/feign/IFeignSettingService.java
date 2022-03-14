package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallbackImpl;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台菜单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignSettingService", value = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignUserFallbackImpl.class)
public interface IFeignSettingService {

	@GetMapping("/sys/tools/setting")
	Result<SettingVO> get(@RequestParam(value = "key") String key);
}
