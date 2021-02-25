package com.taotao.cloud.file.base;

import com.taotao.cloud.file.exception.FileUploadException;
import com.taotao.cloud.file.pojo.FileInfo;
import com.taotao.cloud.file.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件上传抽象类
 *
 * @date 2020/10/26 10:49
 * @since v1.0
 */
public abstract class AbstractFileUpload implements FileUpload {

	private static final String FILE_SPLIT = ".";

	@Override
	public FileInfo upload(File file) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		} else {
			return uploadFile(file, fileInfo);
		}
	}

	@Override
	public FileInfo upload(File file, String fileName) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		} else {
			fileInfo.setName(fileName);
			return uploadFile(file, fileInfo);
		}
	}

	@Override
	public FileInfo upload(MultipartFile file) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getMultipartFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		}
		return uploadFile(file, fileInfo);
	}

	@Override
	public FileInfo upload(MultipartFile file, String fileName) throws FileUploadException {
		FileInfo fileInfo = FileUtil.getMultipartFileInfo(file);
		if (!fileInfo.getName().contains(FILE_SPLIT)) {
			throw new FileUploadException("缺少后缀名");
		}
		fileInfo.setName(fileName);
		return uploadFile(file, fileInfo);
	}

	protected abstract FileInfo uploadFile(MultipartFile file, FileInfo fileInfo) throws FileUploadException;

	protected abstract FileInfo uploadFile(File file, FileInfo fileInfo) throws FileUploadException;

}
