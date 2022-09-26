package com.taotao.cloud.oss;

import cn.hutool.core.io.FileUtil;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.service.StandardOssClient;

public interface StandardOssClientTest {

	default void upLoad() {
		String s = "C:\\Users\\Administrator\\Desktop\\流程配置信息\\附件1：用户信息69.xlsx";
		// OssInfo ossInfo = getOssClient().upLoadWithInputStream(FileUtil.getInputStream(s), OssPathUtil.getTargetName(FileUtil.file(s)));
		OssInfo ossInfo = getOssClient().upLoadWithFile(FileUtil.file(s));
		System.out.println(ossInfo);
	}


	default void upLoadCheckPoint() {
		OssInfo ossInfo = getOssClient().upLoadCheckPoint("/Users/admin/test.zip", "test.zip");
		System.out.println(ossInfo);
	}

	default void downLoad() {
		getOssClient().downLoad(FileUtil.getOutputStream("/Users/admin/test.png"), "test1.png");
	}

	default void downloadCheckPoint() {
		getOssClient().downLoadCheckPoint("/Users/admin/test.zip", "test.zip");
	}

	default void delete() {
		getOssClient().delete("test1.png");
	}

	default void copy() {
		getOssClient().copy("test.png", "test1.png");
	}

	default void move() {
		getOssClient().move("test1.png", "test2.png");
	}

	default void rename() {
		getOssClient().rename("test2.png", "test1.png");
	}

	default void getInfo() {
		OssInfo info = getOssClient().getInfo("test.png");
		System.out.println(info);
		info = getOssClient().getInfo("/", true);
		System.out.println(info);
	}

	default void isExist() {
		System.out.println(getOssClient().isExist("test.png"));
	}

	StandardOssClient getOssClient();

}
