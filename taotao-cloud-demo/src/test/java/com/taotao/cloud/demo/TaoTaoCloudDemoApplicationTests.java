package com.taotao.cloud.demo;

import org.dromara.hutoolcore.io.FileUtil;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaoTaoCloudDemoApplicationTests {

	@Autowired
	private StandardOssClient standardOssClient;

	@Test
	void contextLoads() {
		//upLoad();
		upLoadCheckPoint();
		//getInfo();

		//downLoad();
	}

	public void upLoad() {
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();

		OssInfo ossInfo = getOssClient().upLoad(
			FileUtil.getInputStream("/Users/dengtao/document/商品图片/1561601122001_34.jpg"),
			year + "/" + month + "/" + day + "/" + "qqqqqqq.png");
		System.out.println(ossInfo);
	}

	public void upLoadCheckPoint() {
		OssInfo ossInfo = getOssClient().upLoadCheckPoint("/Users/dengtao/company/1622021703903187.mp4", "111.mp4");
		System.out.println(ossInfo);
	}

	public void downLoad() {
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();

		getOssClient().downLoad(FileUtil.getOutputStream("/Users/dengtao/document/test.png"), year + "/" + month + "/" + day + "/" + "xxxxxxx.png");
	}

	public void downloadCheckPoint() {
		getOssClient().downLoadCheckPoint("/Users/admin/test.zip", "test.zip");
	}

	public void delete() {
		getOssClient().delete("test1.png");
	}

	public void copy() {
		getOssClient().copy("test.png", "test1.png");
	}

	public void move() {
		getOssClient().move("test1.png", "test2.png");
	}

	public void rename() {
		getOssClient().rename("test2.png", "test1.png");
	}

	public void getInfo() {
		OssInfo info = getOssClient().getInfo("test.png");
		System.out.println(info);
		info = getOssClient().getInfo("/", true);
		System.out.println(info);
	}

	public void isExist() {
		System.out.println(getOssClient().isExist("test.png"));
	}

	public StandardOssClient getOssClient() {
		return standardOssClient;
	}

}
