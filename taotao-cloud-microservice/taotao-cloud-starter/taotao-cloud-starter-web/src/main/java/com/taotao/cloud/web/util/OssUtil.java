/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.web.util;

import org.apache.commons.io.FilenameUtils;

/**
 * OSS处理工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 22:59
 */
public class OssUtil {

	public static final String IMAGES_STR = "png,jpg,jpeg,gif,tif,bmp";
	public static final String VIDEO_STR = "avi,wmv,mpeg,mp4,mov,flv,rm,rmvb,3gp";

	/**
	 * 根据文件名获取文件类型：1.图片　2.视频　3.其他
	 *
	 * @param fileName 文件名
	 * @return int
	 * @author shuigedeng
	 * @since 2021/8/24 22:59
	 */
	public static int getFileType(String fileName) {
		String fileType = FilenameUtils.getExtension(fileName);
		assert fileType != null;
		int type = 3;
		if (IMAGES_STR.contains(fileType)) {
			type = 1;
		} else if (VIDEO_STR.contains(fileType)) {
			type = 2;
		}
		return type;
	}
}
