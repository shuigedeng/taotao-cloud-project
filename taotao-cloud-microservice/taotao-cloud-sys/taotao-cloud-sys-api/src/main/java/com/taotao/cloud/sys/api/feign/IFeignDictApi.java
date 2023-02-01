package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.openfeign.api.ApiInfo;
import com.taotao.cloud.openfeign.api.ApiInfo.Create;
import com.taotao.cloud.openfeign.api.ApiInfo.Update;
import com.taotao.cloud.openfeign.api.VersionEnum;
import com.taotao.cloud.sys.api.feign.fallback.FeignDictApiFallback;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, contextId = "feignDictApi", fallbackFactory = FeignDictApiFallback.class)
public interface IFeignDictApi {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link FeignDictResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = VersionEnum.V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = VersionEnum.V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = VersionEnum.V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping("/sys/remote/dict/code")
	FeignDictResponse findByCode(@RequestParam(value = "code") String code);


	/**
	 * 字典列表code查询
	 *
	 * @param id 代码
	 * @return {@link FeignDictResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = VersionEnum.V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = VersionEnum.V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = VersionEnum.V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping("/sys/remote/dict/code")
	FeignDictResponse test(@RequestParam(value = "id") String id);
}

