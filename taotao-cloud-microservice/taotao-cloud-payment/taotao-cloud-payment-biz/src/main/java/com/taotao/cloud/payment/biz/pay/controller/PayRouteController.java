/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.controller;

import cn.hutool.core.util.ObjectUtil;
import com.xhuicloud.common.authorization.resource.annotation.Anonymous;
import com.xhuicloud.common.core.constant.CommonConstants;
import com.xhuicloud.common.core.enums.pay.PayTypeEnum;
import com.xhuicloud.common.core.exception.SysException;
import com.xhuicloud.common.data.ttl.XHuiCommonThreadLocalHolder;
import com.xhuicloud.pay.config.PayConfigInit;
import com.xhuicloud.pay.dto.PayOrderDto;
import com.xhuicloud.pay.handle.impl.AliPayServiceImpl;
import com.xhuicloud.pay.properties.PayProperties;
import com.xhuicloud.pay.utils.UserAgentUtil;
import com.xhuicloud.upms.vo.TenantVo;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: XHuiCloud
 * @description: 支付中心路由
 * @author: Sinda
 * @create: 2020-06-04 14:31
 */
@Controller
@RequestMapping("/route")
@AllArgsConstructor
@Api(value = "route", tags = "支付路由模块")
public class PayRouteController {

	private final AliPayServiceImpl aliPayService;

	private final PayProperties payProperties;

	/**
	 * 聚合支付 被扫
	 *
	 * @param modelAndView
	 * @param request
	 * @return
	 */
	@SneakyThrows
	@GetMapping
	@Anonymous(value = false)
	public ModelAndView toPay(ModelAndView modelAndView,
			HttpServletRequest request) {
		TenantVo tenantVo = getTenant(XHuiCommonThreadLocalHolder.getTenant());
		if (ObjectUtil.isNotNull(tenantVo)) {
			modelAndView.setViewName("ftl/h5pay");
			modelAndView.addObject("tenant", tenantVo);
			if (UserAgentUtil.isWeChat(request)) {
				// 微信
				modelAndView.addObject("channel", PayTypeEnum.WEIXIN_WAP.toString());
			} else {
				// 支付宝
//                String code = request.getParameter("auth_code");
//                if (StringUtils.isEmpty(code)) {
				// 先去获取用户信息 再回来 防止不在对应的客服端打开 后续回调唤起渠道支付带上做处理
//                    modelAndView.setViewName("redirect:https://openauth.alipaydev.com/oauth2/publicAppAuthorize.htm?app_id=" +
//                            PayConfigInit.tenantIdAliPayAppIdMaps.get(FdpTenantHolder.getTenant()) +
//                            "&scope=auth_base&redirect_uri=" +
//                            URLEncoder.encode(payProperties.getDomain(), "utf-8") +
//                            "pay%2froute%3ftenant_id%3d" + FdpTenantHolder.getTenant());
//                } else {
				modelAndView.addObject("channel", PayTypeEnum.ALIPAY_WAP.toString());
//                    modelAndView.addObject("code", code);
//                }
			}
		}
		return modelAndView;
	}

	/**
	 * 唤起渠道支付
	 *
	 * @param payOrderDto  付款实体
	 * @param request
	 * @param modelAndView
	 * @param modelAndView
	 * @return
	 */
	@SneakyThrows
	@GetMapping("/call")
	@Anonymous(value = false)
	public ModelAndView call(PayOrderDto payOrderDto,
			HttpServletRequest request,
			ModelAndView modelAndView) {
		payOrderDto.setQuitUrl(payProperties.getDomain() + "pay/route");
		if (ObjectUtil.isNotNull(getTenant(XHuiCommonThreadLocalHolder.getTenant()))) {
			if (UserAgentUtil.isWeChat(request)) {
				// 唤起微信
				modelAndView.setViewName("ftl/success");
			} else {
				payOrderDto.setGoodsTitle(CommonConstants.SCAN_CODE_PAY);
				payOrderDto.setChannelId(PayTypeEnum.ALIPAY_WAP.getType());
				aliPayService.pay(payOrderDto);
			}
		}
		return modelAndView;
	}

	/**
	 * 获取租户明细
	 *
	 * @param tenantId
	 * @return
	 */
	@Cacheable(value = "Tenant", key = "#tenantId")
	public TenantVo getTenant(Integer tenantId) {
		TenantVo tenantVo = PayConfigInit.tenantMaps.get(tenantId);
		if (ObjectUtil.isNotNull(tenantVo)) {
			XHuiCommonThreadLocalHolder.setTenant(Integer.valueOf(tenantId));
		} else {
			throw SysException.sysFail(SysException.TENANT_NOT_EXIST_DATA_EXCEPTION);
		}
		return tenantVo;
	}

}
