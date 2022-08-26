package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignSettingServiceFallback;
import com.taotao.cloud.sys.api.web.vo.setting.BaseSetting;
import com.taotao.cloud.sys.api.web.vo.setting.ExperienceSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.GoodsSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.OrderSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.PointSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.QQConnectSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.SeckillSetting;
import com.taotao.cloud.sys.api.web.vo.setting.SettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.WechatConnectSettingVO;
import com.taotao.cloud.sys.api.web.vo.setting.payment.AlipayPaymentSetting;
import com.taotao.cloud.sys.api.web.vo.setting.payment.WechatPaymentSetting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台配置模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:48
 */
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignSettingServiceFallback.class)
public interface IFeignSettingService {

	/**
	 * 通过key获取配置
	 *
	 * @param key key
	 * @return 配置信息
	 * @since 2022-03-25 14:10:22
	 */
	@GetMapping("/sys/tools/setting")
	Result<SettingVO> get(@RequestParam(value = "key") String key);

	@GetMapping("/sys/tools/setting/base")
	Result<BaseSetting> getBaseSetting(String name);

	/**
	 * 获得商品设置
	 *
	 * @param name 名字
	 * @return {@link Result }<{@link GoodsSettingVO }>
	 * @since 2022-04-25 16:47:40
	 */
	@GetMapping("/sys/tools/setting/goods")
	Result<GoodsSettingVO> getGoodsSetting(String name);

	@GetMapping("/sys/tools/setting/order")
	Result<OrderSettingVO> getOrderSetting(String name);

	@GetMapping("/sys/tools/setting/experience")
	Result<ExperienceSettingVO> getExperienceSetting(String name);

	@GetMapping("/sys/tools/setting/point")
	Result<PointSettingVO> getPointSetting(String name);


	@GetMapping("/sys/tools/setting/qq/connect")
	Result<QQConnectSettingVO> getQQConnectSetting(String name);

	@GetMapping("/sys/tools/setting/wechat/connect")
	Result<WechatConnectSettingVO> getWechatConnectSetting(String name);

	@GetMapping("/sys/tools/setting/seckill")
	Result<SeckillSetting> getSeckillSetting(String name);

	@GetMapping("/sys/tools/setting/ali")
	Result<AlipayPaymentSetting> getAlipayPaymentSetting(String name);
	@GetMapping("/sys/tools/setting/wechat")
	Result<WechatPaymentSetting> getWechatPaymentSetting(String name);

}
