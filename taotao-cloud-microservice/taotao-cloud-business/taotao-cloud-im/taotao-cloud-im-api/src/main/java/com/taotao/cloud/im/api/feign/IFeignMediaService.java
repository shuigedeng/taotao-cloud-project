package com.taotao.cloud.im.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.media.api.feign.fallback.FeignMediaFallback;
import com.taotao.cloud.media.api.model.vo.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteChatbotService", value = ServiceName.TAOTAO_CLOUD_FILE, fallbackFactory = FeignMediaFallback.class)
public interface IFeignMediaService {

	/**
	 * 根据id查询文件信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.dfs.api.vo.FileVO>
	 * @author shuigedeng
	 * @since 2020/11/20 上午11:17
	 */
	@GetMapping("/file/info/id/{id:[0-9]*}")
	public Result<FileVO> findFileById(@PathVariable(value = "id") Long id);
}

