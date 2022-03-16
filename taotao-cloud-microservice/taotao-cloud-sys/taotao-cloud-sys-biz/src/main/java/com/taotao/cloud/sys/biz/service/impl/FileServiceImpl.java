package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.service.UploadFileService;
import com.taotao.cloud.sys.biz.entity.file.File;
import com.taotao.cloud.sys.biz.mapper.IFileMapper;
import com.taotao.cloud.sys.biz.repository.cls.FileRepository;
import com.taotao.cloud.sys.biz.repository.inf.IFileRepository;
import com.taotao.cloud.sys.biz.service.IFileService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/12 17:43
 */
@Service
public class FileServiceImpl extends
	BaseSuperServiceImpl<IFileMapper, File, FileRepository, IFileRepository, Long>
	implements IFileService {

	@Autowired(required = false)
	private UploadFileService uploadFileService;

	@Override
	public File upload(MultipartFile file) {
		try {
			UploadFileInfo upload = uploadFileService.upload(file);

			// 添加文件数据
			return new File();
		} catch (UploadFileException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public File findFileById(Long id) {
		Optional<File> optionalFile = ir().findById(id);
		return optionalFile.orElseThrow(() -> new BusinessException(ResultEnum.FILE_NOT_EXIST));
	}
	//
	// @Override
	// public Result<Object> delete(String objectName) {
	// 	try {
	// 		ossClient.deleteObject(ossConfig.getBucketName(), objectName);
	// 	} catch (OSSException | ClientException e) {
	// 		e.printStackTrace();
	// 		return new Result<>(ResultEnum.ERROR.getCode(), "删除文件失败");
	// 	}
	// 	return new Result<>();
	// }
	//
	// @Override
	// public Result<List<OSSObjectSummary>> list() {
	// 	// 设置最大个数。
	// 	final int maxKeys = 100;
	//
	// 	ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(ossConfig.getBucketName()).withMaxKeys(maxKeys));
	// 	List<OSSObjectSummary> result = objectListing.getObjectSummaries();
	// 	return new Result<List<OSSObjectSummary>>(result);
	// }
	//
	// @Override
	// public void exportOssFile(ServletOutputStream outputStream, String objectName) {
	// 	OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), objectName);
	//
	// 	BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
	// 	BufferedOutputStream out = new BufferedOutputStream(outputStream);
	// 	try {
	// 		byte[] buffer = new byte[1024];
	// 		int lenght;
	// 		while ((lenght = in.read(buffer)) != -1) {
	// 			out.write(buffer, 0, lenght);
	// 		}
	//
	// 		out.flush();
	// 		out.close();
	// 		in.close();
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }
}
