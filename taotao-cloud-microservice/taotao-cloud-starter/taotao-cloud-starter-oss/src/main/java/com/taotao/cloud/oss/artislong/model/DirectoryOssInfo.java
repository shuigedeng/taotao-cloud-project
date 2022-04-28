package com.taotao.cloud.oss.artislong.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹信息对象
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:20
 */
public class DirectoryOssInfo extends OssInfo {

	/**
	 * 文件夹列表
	 */
	private List<FileOssInfo> fileInfos = new ArrayList<>();

	/**
	 * 文件列表
	 */
	private List<DirectoryOssInfo> directoryInfos = new ArrayList<>();

	public DirectoryOssInfo() {
	}

	public DirectoryOssInfo(List<FileOssInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public List<FileOssInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileOssInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public List<DirectoryOssInfo> getDirectoryInfos() {
		return directoryInfos;
	}

	public void setDirectoryInfos(
		List<DirectoryOssInfo> directoryInfos) {
		this.directoryInfos = directoryInfos;
	}
}
