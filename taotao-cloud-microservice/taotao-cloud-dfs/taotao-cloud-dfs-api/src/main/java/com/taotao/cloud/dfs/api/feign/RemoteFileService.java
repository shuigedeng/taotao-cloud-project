package com.taotao.cloud.dfs.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.dfs.api.feign.fallback.RemoteFileFallbackImpl;
import com.taotao.cloud.dfs.api.vo.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用售后模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteChatbotService", value = ServiceNameConstant.TAOTAO_CLOUD_FILE_CENTER, fallbackFactory = RemoteFileFallbackImpl.class)
public interface RemoteFileService {

	/**
	 * 根据id查询文件信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.dfs.api.vo.FileVO>
	 * @author dengtao
	 * @date 2020/11/20 上午11:17
	 * @since v1.0
	 */
	@GetMapping("/file/info/id/{id:[0-9]*}")
	public Result<FileVO> findFileById(@PathVariable(value = "id") Long id);
}

