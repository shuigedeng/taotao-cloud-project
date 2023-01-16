package com.taotao.cloud.promotion.biz.task;

import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexApi;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.service.business.SeckillService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.SeckillSetting;
import com.taotao.cloud.web.timetask.EveryDayExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 促销活动每日定时器
 */

@Component
public class PromotionEverydayExecute implements EveryDayExecute {

	/**
	 * ES商品索引
	 */
	@Autowired
	private IFeignEsGoodsIndexApi esGoodsIndexService;
	/**
	 * 系统设置
	 */
	@Autowired
	private IFeignSettingApi settingService;
	/**
	 * 秒杀活动
	 */
	@Autowired
	private SeckillService seckillService;

	/**
	 * 将已过期的促销活动置为结束
	 */
	@Override
	public void execute() {
		//清除所以商品索引的无效促销活动
		this.esGoodsIndexService.cleanInvalidPromotion();
		//定时创建活动
		addSeckill();
	}

	/**
	 * 添加秒杀活动
	 * 从系统设置中获取秒杀活动的配置
	 * 添加30天后的秒杀活动
	 */
	private void addSeckill() {
		SeckillSetting seckillSetting = settingService.getSeckillSetting(SettingCategoryEnum.SECKILL_SETTING.name());
		for (int i = 1; i <= SeckillService.PRE_CREATION; i++) {
			Seckill seckill = new Seckill(i, seckillSetting.getHours(), seckillSetting.getSeckillRule());
			seckillService.savePromotions(seckill);
		}
	}
}
