package com.taotao.cloud.system.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dfs.api.feign.RemoteFileService;
import com.taotao.cloud.dfs.api.vo.FileVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RemoteFileFallbackImpl implements FallbackFactory<RemoteFileService> {

	@Override
	public RemoteFileService create(Throwable throwable) {
		return new RemoteFileService() {
			@Override
			public Result<FileVO> findFileById(Long id) {
				LogUtil.error("调用findFileById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
