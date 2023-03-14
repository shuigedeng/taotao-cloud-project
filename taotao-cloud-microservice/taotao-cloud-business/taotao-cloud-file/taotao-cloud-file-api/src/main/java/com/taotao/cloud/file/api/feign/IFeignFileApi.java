package com.taotao.cloud.file.api.feign;

import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_07;
import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_08;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.file.api.feign.fallback.FeignFileApiFallback;
import com.taotao.cloud.file.api.feign.response.FeignFileResponse;
import com.taotao.cloud.openfeign.api.ApiInfo;
import com.taotao.cloud.openfeign.api.ApiInfo.Create;
import com.taotao.cloud.openfeign.api.ApiInfo.Update;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_FILE, contextId = "feignDictApi", fallbackFactory = FeignFileApiFallback.class)
public interface IFeignFileApi {

	/**
	 * 字典列表code查询
	 *
	 * @param code 代码
	 * @return {@link FeignFileResponse }
	 * @since 2022-06-29 21:40:21
	 */
	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping("/sys/feign/dict/code")
	FeignFileResponse findByCode(@RequestParam(value = "code") String code);


}

