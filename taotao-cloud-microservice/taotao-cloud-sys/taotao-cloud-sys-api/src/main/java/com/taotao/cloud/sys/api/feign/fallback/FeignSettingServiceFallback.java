package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.model.vo.setting.BaseSetting;
import com.taotao.cloud.sys.api.model.vo.setting.ExperienceSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.GoodsSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.OrderSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.PointSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.QQConnectSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.SeckillSetting;
import com.taotao.cloud.sys.api.model.vo.setting.SettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.WechatConnectSettingVO;
import com.taotao.cloud.sys.api.model.vo.setting.payment.AlipayPaymentSetting;
import com.taotao.cloud.sys.api.model.vo.setting.payment.WechatPaymentSetting;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignSettingFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignSettingServiceFallback implements FallbackFactory<IFeignSettingService> {

	@Override
	public IFeignSettingService create(Throwable throwable) {
		return new IFeignSettingService() {
			@Override
			public Result<SettingVO> get(String key) {
				return null;
			}

			@Override
			public Result<BaseSetting> getBaseSetting(String name) {
				return null;
			}

			@Override
			public Result<GoodsSettingVO> getGoodsSetting(
				String name) {
				return null;
			}

			@Override
			public Result<OrderSettingVO> getOrderSetting(String name) {
				return null;
			}

			@Override
			public Result<ExperienceSettingVO> getExperienceSetting(String name) {
				return null;
			}

			@Override
			public Result<PointSettingVO> getPointSetting(String name) {
				return null;
			}

			@Override
			public Result<QQConnectSettingVO> getQQConnectSetting(String name) {
				return null;
			}

			@Override
			public Result<WechatConnectSettingVO> getWechatConnectSetting(String name) {
				return null;
			}

			@Override
			public Result<SeckillSetting> getSeckillSetting(String name) {
				return null;
			}

			@Override
			public Result<AlipayPaymentSetting> getAlipayPaymentSetting(String name) {
				return null;
			}

			@Override
			public Result<WechatPaymentSetting> getWechatPaymentSetting(String name) {
				return null;
			}
		};
	}
}
