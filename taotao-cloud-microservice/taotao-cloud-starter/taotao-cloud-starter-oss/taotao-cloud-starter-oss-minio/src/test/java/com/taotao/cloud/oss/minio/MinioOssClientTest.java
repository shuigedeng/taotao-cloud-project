package com.taotao.cloud.oss.minio;


import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.minio.support.MinioOssConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = MinioOssClientTest.App.class,
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class MinioOssClientTest implements StandardOssClientTest {
	@SpringBootApplication
	public static class App {

	}

	@Autowired
	@Qualifier(MinioOssConfiguration.DEFAULT_BEAN_NAME)
	private StandardOssClient ossClient;

	@Test
	public void test() throws Exception {
		upLoad();
		downLoad();
		copy();
		rename();
		move();
		isExist();
		getInfo();
		delete();

		upLoadCheckPoint();
		downloadCheckPoint();
	}

	@Override
	public StandardOssClient getOssClient() {
		return ossClient;
	}
}
