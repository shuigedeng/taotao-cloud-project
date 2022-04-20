package com.taotao.cloud.sys.biz;

import com.taotao.cloud.sys.biz.service.IFileService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaoTaoCloudSysApplicationTests {

	@Autowired
	private IFileService fileService;

	@Test
	void contextLoads() {
		fileService.findFileById(1L);
	}

	@Test
	public void test1(){
		fileService.findFileById(1L);
	}

}
