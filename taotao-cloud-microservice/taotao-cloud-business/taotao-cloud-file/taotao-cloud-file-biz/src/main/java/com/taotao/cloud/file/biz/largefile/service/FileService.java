package com.taotao.cloud.file.biz.largefile.service;


import com.taotao.cloud.file.biz.largefile.po.FileUpload;
import com.taotao.cloud.file.biz.largefile.po.FileUploadRequest;
import java.io.IOException;

public interface FileService {

	FileUpload upload(FileUploadRequest fileUploadRequestDTO) throws IOException;

	FileUpload sliceUpload(FileUploadRequest fileUploadRequestDTO);

	FileUpload checkFileMd5(FileUploadRequest fileUploadRequestDTO) throws IOException;

}
