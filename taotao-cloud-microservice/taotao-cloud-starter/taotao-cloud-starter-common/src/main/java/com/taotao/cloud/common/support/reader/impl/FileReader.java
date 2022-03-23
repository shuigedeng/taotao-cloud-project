package com.taotao.cloud.common.support.reader.impl;


import com.taotao.cloud.common.support.reader.IReader;
import com.taotao.cloud.common.utils.io.FileUtil;
import java.io.File;

/**
 * 读取类
 */
public class FileReader implements IReader {

	/**
	 * 文件路径
	 */
	private final File file;

	/**
	 * 文件编码
	 */
	private final String charset;

	public FileReader(File file, String charset) {
		this.file = file;
		this.charset = charset;
	}

	@Override
	public String read() {
		return FileUtil.getFileContent(file, charset);
	}

}
