package com.taotao.cloud.open.platform.openapiclient;

import cn.hutool.core.io.FileUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.openapi.common.model.FileBinary;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class FileApiTest {

	private static final String dir = FileApiTest.class.getResource("/test").getPath();

	@Autowired
	FileApiClient fileApiClient;


	public void uploadTest() {
		LogUtils.info("upload start...");
		long startTime = System.currentTimeMillis();
		File src = new File(dir, "001.txt");
		byte[] fileBytes = FileUtil.readBytes(src);

		FileBinary file1 = new FileBinary();
		file1.setData(fileBytes);
		file1.setFileName(src.getName());
		fileApiClient.upload(10L, file1, file1);
		LogUtils.info("upload end. costTime={}", System.currentTimeMillis() - startTime);
	}

	public void batchUploadTest() {
		LogUtils.info("batchUpload start...");
		long startTime = System.currentTimeMillis();
		File src = new File(dir, "001.txt");
		byte[] fileBytes = FileUtil.readBytes(src);

		FileBinary file1 = new FileBinary();
		file1.setData(fileBytes);
		file1.setFileName(src.getName());

		FileBinary[] fileBinaries = new FileBinary[2];
		fileBinaries[0] = file1;
		fileBinaries[1] = file1;
		fileApiClient.upload(101L, fileBinaries);
		LogUtils.info("batchUpload end. costTime={}", System.currentTimeMillis() - startTime);
	}

	public void batchUpload2Test() {
		LogUtils.info("batchUpload start...");
		long startTime = System.currentTimeMillis();
		File src = new File(dir, "001.txt");
		byte[] fileBytes = FileUtil.readBytes(src);

		FileBinary file1 = new FileBinary();
		file1.setData(fileBytes);
		file1.setFileName(src.getName());

		List<FileBinary> fileBinaries = new ArrayList<>();
		fileBinaries.add(file1);
		fileBinaries.add(file1);
		fileApiClient.upload(102L, fileBinaries);
		LogUtils.info("batchUpload end. costTime={}", System.currentTimeMillis() - startTime);
	}


	public void downloadTest() {
		LogUtils.info("download start...");
		long startTime = System.currentTimeMillis();
		FileBinary fileBinary = fileApiClient.download(1L);
		File dest = new File(dir, "download/" + fileBinary.getFileName());
		byte[] fileBytes = fileBinary.getData();
		FileUtil.writeBytes(fileBytes, dest);
		LogUtils.info("download end. costTime={}, file={}", System.currentTimeMillis() - startTime,
			fileBinary);
	}
}
