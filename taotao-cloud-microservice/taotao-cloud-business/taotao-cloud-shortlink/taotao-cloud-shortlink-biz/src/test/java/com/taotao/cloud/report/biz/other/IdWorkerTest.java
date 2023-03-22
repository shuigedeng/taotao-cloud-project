package com.taotao.cloud.report.biz.other;

import org.junit.Test;
import shorturl.server.server.application.util.IdWorker;
import shorturl.server.server.application.util.IdWorkerInstance;

public class IdWorkerTest {

	IdWorker idWorker = IdWorkerInstance.INSTANCE.IdWorkerInstance();

	@Test
	public void nextId() {
		for (int i = 0; i < 10; i++) {
			long id = idWorker.generate();
			System.out.println("生成id为:" + id);
		}
	}

}

