package com.taotao.cloud.sys.biz.config.init;

import com.taotao.cloud.sys.biz.service.business.IRegionService;
import com.taotao.cloud.sys.biz.service.business.IVisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化站点统计
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:05
 */
@Component
public class VisitsInit implements ApplicationRunner {

	@Autowired
	private IVisitsService visitsService;
	@Autowired
	private IRegionService regionService;

	@Override
	public void run(ApplicationArguments args) {
		//System.out.println("--------------- 初始化站点统计，如果存在今日统计则跳过 ---------------");
		//IVisitsService.save();
		//System.out.println("--------------- 初始化站点统计完成 ---------------");
		//
		//regionService.synchronizationData("");
	}
}
