package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.biz.entity.MemberRecharge;
import com.taotao.cloud.order.api.query.recharge.RechargePageQuery;

/**
 * 预存款充值业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 13:21:47
 */
public interface MemberRechargeService extends IService<MemberRecharge> {

	/**
	 * 创建充值订单
	 *
	 * @param price 价格
	 * @return {@link MemberRecharge }
	 * @since 2022-05-31 13:21:47
	 */
	MemberRecharge recharge(Double price);

	/**
	 * 查询充值订单列表
	 *
	 * @param rechargePageQuery 查询条件
	 * @return {@link IPage }<{@link MemberRecharge }>
	 * @since 2022-05-31 13:21:47
	 */
	IPage<MemberRecharge> rechargePage(RechargePageQuery rechargePageQuery);


	/**
	 * 支付成功
	 *
	 * @param sn            充值订单编号
	 * @param receivableNo  流水no
	 * @param paymentMethod 支付方式
	 * @since 2022-05-31 13:21:47
	 */
	void paySuccess(String sn, String receivableNo, String paymentMethod);

	/**
	 * 根据充值订单号查询充值信息
	 *
	 * @param sn 充值订单号
	 * @return {@link MemberRecharge }
	 * @since 2022-05-31 13:21:47
	 */
	MemberRecharge getRecharge(String sn);

	/**
	 * 充值订单取消
	 *
	 * @param sn 充值订单sn
	 * @since 2022-05-31 13:21:47
	 */
	void rechargeOrderCancel(String sn);

}
