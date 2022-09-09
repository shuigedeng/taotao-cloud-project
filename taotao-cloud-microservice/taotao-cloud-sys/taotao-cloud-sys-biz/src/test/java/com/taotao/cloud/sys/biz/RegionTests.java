package com.taotao.cloud.sys.biz;

import com.taotao.cloud.sys.biz.service.business.IFileService;
import com.taotao.cloud.sys.biz.service.business.IRegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegionTests {

	@Autowired
	private IFileService fileService;
	@Autowired
	private IRegionService regionService;

	@Test
	void contextLoads() {
		fileService.findFileById(1L);
	}

	@Test
	public void synchronizationData(){
		regionService.synchronizationData(null);
	}

}
