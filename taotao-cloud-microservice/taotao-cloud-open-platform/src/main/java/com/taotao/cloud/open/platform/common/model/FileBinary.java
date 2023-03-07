package com.taotao.cloud.open.platform.common.model;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.open.platform.common.util.CommonUtil;
import com.taotao.cloud.open.platform.common.util.TruncateUtil;

/**
 * 文件类型
 * <p>
 * 用于文件传输，用在方法参数上或方法返回值当中，可以提升文件传输效率
 * </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:08
 */
public class FileBinary extends Binary {

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件类型
	 */
	private String fileType;

	@Override
	public String toString() {
		FileBinary binary = CommonUtil.cloneInstance(this);
		long length = binary.getLength();
		binary.setDataStr(TruncateUtil.truncate(binary.getData()));
		binary.setData(null);
		binary.setLength(length);
		return JSONUtil.toJsonStr(binary);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
