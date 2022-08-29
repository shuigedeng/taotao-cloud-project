package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * 文件测试
 *
 */
public class FileTest {

	@Test
	public void test1() {
		String fileName = "123123123.xx.jpg";
		String fileExtension = FileUtils.getFileExtension(fileName);
		Assert.assertEquals("jpg", fileExtension);
		String extensionWithDot = FileUtils.getFileExtensionWithDot(fileName);
		Assert.assertEquals(".jpg", extensionWithDot);
		String pathWithoutExtension = FileUtils.getPathWithoutExtension(fileName);
		Assert.assertEquals("123123123.xx", pathWithoutExtension);
	}

	@Test
	public void test2() {
		String data = "123哈哈";
		String testFilePath = FileUtils.getTempDirPath() + File.separator + "1.txt";
		File testFile = new File(testFilePath);
		FileUtils.writeToFile(testFile, data, false);
		String read1 = FileUtils.readToString(testFile);
		Assert.assertEquals(data, read1);
		String read2 = FileUtils.readToString(testFile);
		Assert.assertEquals(data, read2);
		FileUtils.writeToFile(testFile, data, true);
		String read3 = FileUtils.readToString(testFile);
		Assert.assertEquals(data + data, read3);
		testFile.deleteOnExit();
	}

}
