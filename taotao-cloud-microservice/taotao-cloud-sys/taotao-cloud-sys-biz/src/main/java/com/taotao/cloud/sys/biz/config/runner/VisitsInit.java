package com.taotao.cloud.sys.biz.config.runner;

import com.taotao.cloud.sys.biz.service.IRegionService;
import com.taotao.cloud.sys.biz.service.IVisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化站点统计
 */
@Component
public class VisitsInit implements ApplicationRunner {

	private final IVisitsService IVisitsService;

	@Autowired
	private IRegionService regionService;

	public VisitsInit(IVisitsService IVisitsService) {
		this.IVisitsService = IVisitsService;
	}

	@Override
	public void run(ApplicationArguments args) {
		//System.out.println("--------------- 初始化站点统计，如果存在今日统计则跳过 ---------------");
		//IVisitsService.save();
		//System.out.println("--------------- 初始化站点统计完成 ---------------");
		//
		//regionService.synchronizationData("");
	}
}
