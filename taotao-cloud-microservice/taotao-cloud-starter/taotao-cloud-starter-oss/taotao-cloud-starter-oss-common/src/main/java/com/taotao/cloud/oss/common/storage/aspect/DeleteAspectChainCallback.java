package com.taotao.cloud.oss.common.storage.aspect;


import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
