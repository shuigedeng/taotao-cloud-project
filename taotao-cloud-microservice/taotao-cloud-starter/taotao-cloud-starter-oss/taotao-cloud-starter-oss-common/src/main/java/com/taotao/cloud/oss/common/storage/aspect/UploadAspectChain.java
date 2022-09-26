package com.taotao.cloud.oss.common.storage.aspect;

import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.UploadPretreatment;
import com.taotao.cloud.oss.common.storage.platform.FileStorage;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

/**
 * 上传的切面调用链
 */
@Getter
@Setter
public class UploadAspectChain {

	private UploadAspectChainCallback callback;
	private Iterator<FileStorageAspect> aspectIterator;

	public UploadAspectChain(Iterable<FileStorageAspect> aspects, UploadAspectChainCallback callback) {
		this.aspectIterator = aspects.iterator();
		this.callback = callback;
	}

	/**
	 * 调用下一个切面
	 */
	public FileInfo next(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage,
						 FileRecorder fileRecorder) {
		//还有下一个
		if (aspectIterator.hasNext()) {
			return aspectIterator.next().uploadAround(this, fileInfo, pre, fileStorage, fileRecorder);
		} else {
			return callback.run(fileInfo, pre, fileStorage, fileRecorder);
		}
	}
}
