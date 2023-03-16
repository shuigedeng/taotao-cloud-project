//package com.taotao.cloud.goods.biz.task;
//
//import com.taotao.cloud.goods.biz.service.business.ICommodityService;
//import com.taotao.cloud.job.xxl.timetask.EveryHourExecute;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 小程序直播状态获取
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-27 16:54:43
// */
//@Component
//public class BroadcastExecute implements EveryHourExecute {
//
//	/**
//	 * 商品服务
//	 */
//	@Autowired
//	private ICommodityService commodityService;
//
//	/**
//	 * 执行
//	 */
//	@Override
//	public void execute() {
//		//同步直播商品状态
//		commodityService.getGoodsWareHouse();
//	}
//}
