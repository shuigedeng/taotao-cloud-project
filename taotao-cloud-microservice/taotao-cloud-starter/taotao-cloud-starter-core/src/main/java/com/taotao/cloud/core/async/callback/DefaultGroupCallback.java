package com.taotao.cloud.core.async.callback;

import com.taotao.cloud.core.async.wrapper.WorkerWrapper;
import java.util.List;

/**
 * 默认组回调
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:23:39
 */
public class DefaultGroupCallback implements IGroupCallback {

	@Override
	public void success(List<WorkerWrapper> workerWrappers) {

	}

	@Override
	public void failure(List<WorkerWrapper> workerWrappers, Exception e) {

	}
}
