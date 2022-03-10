package com.taotao.cloud.media.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.media.api.feign.RemoteFileService;
import com.taotao.cloud.media.api.vo.FileVO;
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
				LogUtil.error("调用findFileById异常：{0}", throwable, id);
				return Result.fail("调用findFileById异常", 500);
			}
		};
	}
}
