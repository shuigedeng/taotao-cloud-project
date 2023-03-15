package com.taotao.cloud.file.biz.largefile.callable;


import com.taotao.cloud.file.biz.largefile.context.UploadContext;
import com.taotao.cloud.file.biz.largefile.enu.UploadModeEnum;
import com.taotao.cloud.file.biz.largefile.po.FileUpload;
import com.taotao.cloud.file.biz.largefile.po.FileUploadRequest;
import java.util.concurrent.Callable;

public class FileCallable implements Callable<FileUpload> {

	private UploadModeEnum mode;

	private FileUploadRequest param;

	public FileCallable(UploadModeEnum mode,
		FileUploadRequest param) {

		this.mode = mode;
		this.param = param;
	}

	@Override
	public FileUpload call() throws Exception {

		FileUpload fileUploadDTO = UploadContext.INSTANCE.getInstance(mode).sliceUpload(param);
		return fileUploadDTO;
	}
}
