package com.taotao.cloud.core.runner;

import com.taotao.cloud.common.base.CoreProperties;
import com.taotao.cloud.common.utils.LogUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

/**
 * 容器生命周期监听程序
 * */
@Order
public class CoreApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments var1) throws Exception {
		saveStatus("STARTED");
	}

	private void saveStatus(String status){
		HashMap<String,Object> map = new HashMap<>(2);
		map.put("data",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("state",status);
		LogUtil.info(CoreProperties.Project,"应用已正常启动!");
	}

}
