package com.taotao.cloud.file.biz.filestorage.config;

import com.taotao.cloud.oss.common.storage.platform.LocalFileStorage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 动态存储平台设置
 */
@Component
public class DynamicFileStorage {

	@Autowired
	private List<LocalFileStorage> list;

	public void add() {
		//TODO 读取数据库配置
		LocalFileStorage localFileStorage = new LocalFileStorage();
		localFileStorage.setPlatform("my-local-1");//平台名称
		localFileStorage.setBasePath("");
		localFileStorage.setDomain("");
		list.add(localFileStorage);
	}

	public void remove(String platform) {
		for (LocalFileStorage localFileStorage : list) {

		}
	}
}
