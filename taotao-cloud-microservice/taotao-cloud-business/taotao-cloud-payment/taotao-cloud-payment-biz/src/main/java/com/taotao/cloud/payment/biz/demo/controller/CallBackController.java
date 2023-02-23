package com.taotao.cloud.payment.biz.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.yungouos.pay.util.PaySignUtil;
import com.yungouos.springboot.demo.config.AliPayConfig;
import com.yungouos.springboot.demo.config.WxPayConfig;
import com.yungouos.springboot.demo.service.order.OrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/callback")
public class CallBackController {

	@Resource
	private OrderService orderService;

	@RequestMapping("/notify")
	public String notify(@RequestParam Map<String, String> data, HttpServletRequest request,
			HttpServletResponse response) {
		try {

			System.out.println("接受到支付结果回调");
			System.out.println(data.toString());

			String payChannel = data.get("payChannel");

			String attach = data.get("attach");

			if (StrUtil.isBlank(payChannel)) {
				return "payChannel is not null";
			}

			String key = null;
			if ("wxpay".equals(payChannel)) {
				key = WxPayConfig.key;
			}
			if ("alipay".equals(payChannel)) {
				key = AliPayConfig.key;
			}

			boolean sign = PaySignUtil.checkNotifySign(request, key);

			System.out.println("签名验证：" + sign);

			if (!sign) {
				return "sign fail";
			}

			String outTradeNo = data.get("outTradeNo");
			String payNo = data.get("payNo");
			String time = data.get("time");
			String code = data.get("code");

			if (Integer.valueOf(code).intValue() != 1) {
				return "pay fail";
			}

			boolean success = orderService.paySuccess(outTradeNo, payNo, time);
			if (!success) {
				return "fail";
			}
			PrintWriter out = response.getWriter();
			out.print("SUCCESS");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
