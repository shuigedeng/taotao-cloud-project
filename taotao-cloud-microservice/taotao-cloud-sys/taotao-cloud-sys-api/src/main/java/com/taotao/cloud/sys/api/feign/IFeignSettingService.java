package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallback;
import com.taotao.cloud.sys.api.vo.setting.GoodsSettingVO;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台配置模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:48
 */
@FeignClient(contextId = "IFeignSettingService", value = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignUserFallback.class)
public interface IFeignSettingService {

	/**
	 * 通过key获取配置
	 *
	 * @param key key
	 * @return 配置信息
	 * @since 2022-03-25 14:10:22
	 */
	@GetMapping("/sys/tools/setting")
	Result<SettingVO> get(@RequestParam(value = "key") String key);

	@GetMapping("/sys/tools/setting/goods")
	Result<GoodsSettingVO> getGoodsSetting(String name);
}
