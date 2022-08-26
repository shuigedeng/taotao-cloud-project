package com.taotao.cloud.sys.biz.service.business;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.model.entity.system.Visits;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;

/**
 * VisitsService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:22:47
 */
public interface IVisitsService extends IService<Visits> {

	/**
	 * 提供给定时任务，每天0点执行
	 */
	void save();

	/**
	 * 新增记录
	 *
	 * @param request /
	 */
	@Async
	void count(HttpServletRequest request);

	/**
	 * 获取数据
	 *
	 * @return /
	 */
	Object get();

	/**
	 * getChartData
	 *
	 * @return /
	 */
	Object getChartData();
}
