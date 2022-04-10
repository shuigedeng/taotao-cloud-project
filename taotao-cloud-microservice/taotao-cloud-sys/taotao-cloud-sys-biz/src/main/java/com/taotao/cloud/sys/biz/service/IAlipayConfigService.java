/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.vo.alipay.TradeVO;
import com.taotao.cloud.sys.biz.entity.config.AlipayConfig;

/**
 * ali支付配置服务
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:24:04
 */
public interface IAlipayConfigService extends IService<AlipayConfig> {

	/**
	 * 处理来自PC的交易请求
	 *
	 * @param alipay 支付宝配置
	 * @param trade  交易详情
	 * @return String
	 * @throws Exception 异常
	 */
	String toPayAsPc(AlipayConfig alipay, TradeVO trade) throws Exception;

	/**
	 * 处理来自手机网页的交易请求
	 *
	 * @param alipay 支付宝配置
	 * @param trade  交易详情
	 * @return String
	 * @throws Exception 异常
	 */
	String toPayAsWeb(AlipayConfig alipay, TradeVO trade) throws Exception;

	/**
	 * 查询配置
	 *
	 * @return 配置信息
	 */
	AlipayConfig find();

	/**
	 * 更新配置
	 *
	 * @param alipayConfig 支付宝配置
	 * @return 是否完成更新
	 */
	Boolean update(AlipayConfig alipayConfig);
}
