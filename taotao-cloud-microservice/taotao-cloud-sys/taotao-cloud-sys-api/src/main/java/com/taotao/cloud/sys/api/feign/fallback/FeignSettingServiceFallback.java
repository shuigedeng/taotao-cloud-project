package com.taotao.cloud.sys.api.feign.fallback;

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
			public SettingVO get(String key) {
				return null;
			}

			@Override
			public BaseSetting getBaseSetting(String name) {
				return null;
			}

			@Override
			public GoodsSettingVO getGoodsSetting(
				String name) {
				return null;
			}

			@Override
			public OrderSettingVO getOrderSetting(String name) {
				return null;
			}

			@Override
			public ExperienceSettingVO getExperienceSetting(String name) {
				return null;
			}

			@Override
			public PointSettingVO getPointSetting(String name) {
				return null;
			}

			@Override
			public QQConnectSettingVO getQQConnectSetting(String name) {
				return null;
			}

			@Override
			public WechatConnectSettingVO getWechatConnectSetting(String name) {
				return null;
			}

			@Override
			public SeckillSetting getSeckillSetting(String name) {
				return null;
			}

			@Override
			public AlipayPaymentSetting getAlipayPaymentSetting(String name) {
				return null;
			}

			@Override
			public WechatPaymentSetting getWechatPaymentSetting(String name) {
				return null;
			}
		};
	}
}
