package com.taotao.cloud.payment.biz.demo.service.wxpay;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yungouos.pay.common.PayException;
import com.yungouos.pay.wxpay.WxPay;
import com.yungouos.springboot.demo.config.WxPayConfig;
import com.yungouos.springboot.demo.entity.Order;
import com.yungouos.springboot.demo.service.order.OrderService;

@Service
public class WxPayServiceImpl implements WxPayService {

	@Resource
	private OrderService orderService;

	@Override
	public Map<String, Object> nativePay(String body, String money) {
		Map<String, Object> map = null;
		try {
			Order order = orderService.add(body, money);
			if (order == null) {
				throw new Exception("订单保存失败");
			}
			map = new HashMap<String, Object>();
			String notify_url = "http://yungouos.wicp.net/api/callback/notify";
			String result = WxPay.nativePay(order.getOrderNo(), order.getMoney(), WxPayConfig.mchId, order.getBody(), "2", null, notify_url, null, null, null, null, WxPayConfig.key);
			map.put("url", result);
			map.put("orderNo", order.getOrderNo());
		} catch (PayException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
