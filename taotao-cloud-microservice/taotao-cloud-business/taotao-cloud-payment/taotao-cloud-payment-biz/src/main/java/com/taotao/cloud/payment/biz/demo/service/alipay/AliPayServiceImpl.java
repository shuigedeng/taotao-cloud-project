package com.taotao.cloud.payment.biz.demo.service.alipay;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yungouos.pay.alipay.AliPay;
import com.yungouos.pay.entity.AliPayH5Biz;
import com.yungouos.springboot.demo.config.AliPayConfig;
import com.yungouos.springboot.demo.entity.Order;
import com.yungouos.springboot.demo.service.order.OrderService;

@Service
public class AliPayServiceImpl implements AliPayService {

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
			String attach = "我是一个参数";

			String notify_url = "http://yungouos.wicp.net/api/callback/notify";

			String url = AliPay.nativePay(order.getOrderNo(), order.getMoney(), AliPayConfig.mchId, order.getBody(), "2", attach, notify_url, null, null, null, null, AliPayConfig.key);
			map.put("url", url);
			map.put("orderNo", order.getOrderNo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> mobilePay(String body, String money) {
		Map<String, Object> map = null;
		try {
			Order order = orderService.add(body, money);
			if (order == null) {
				throw new Exception("订单保存失败");
			}
			map = new HashMap<String, Object>();
			String attach = "我是一个参数";

			String notify_url = "http://yungouos.wicp.net/api/callback/notify";

			String return_url="http://yungouos.wicp.net?orderNo="+order.getOrderNo();
			
			AliPayH5Biz aliPayH5Biz = AliPay.h5Pay(order.getOrderNo(), order.getMoney(), AliPayConfig.mchId, order.getBody(), attach, notify_url, return_url, null, null, null, null, AliPayConfig.key);
			
			if(aliPayH5Biz==null){
				throw new Exception("支付宝下单失败");
			}
			
			map.put("url", aliPayH5Biz.getUrl());
			map.put("orderNo", order.getOrderNo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
