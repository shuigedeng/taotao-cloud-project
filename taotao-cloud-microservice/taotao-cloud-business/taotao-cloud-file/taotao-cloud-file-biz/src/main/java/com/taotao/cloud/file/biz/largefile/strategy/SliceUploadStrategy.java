package com.taotao.cloud.file.biz.largefile.strategy;


import com.taotao.cloud.file.biz.largefile.po.FileUpload;
import com.taotao.cloud.file.biz.largefile.po.FileUploadRequest;

public interface SliceUploadStrategy {

	FileUpload sliceUpload(FileUploadRequest param);
}
