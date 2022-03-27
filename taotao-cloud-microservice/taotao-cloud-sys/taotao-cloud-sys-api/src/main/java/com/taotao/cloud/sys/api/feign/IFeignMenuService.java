package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallback;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台菜单模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:10
 */
@FeignClient(contextId = "IFeignResourceService", value = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignUserFallback.class)
public interface IFeignMenuService {

	/**
	 * 根据角色code列表获取角色列表
	 *
	 * @param codes 角色code列表
	 * @return 角色列表
	 * @since 2020/10/21 15:24
	 */
	@GetMapping("/resource/info/codes")
	Result<List<MenuQueryVO>> findResourceByCodes(@RequestParam(value = "codes") Set<String> codes);

}
