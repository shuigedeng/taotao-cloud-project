package com.taotao.cloud.oss.common.storage.aspect;


import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.UploadPretreatment;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {
    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage,
	    FileRecorder fileRecorder);
}
