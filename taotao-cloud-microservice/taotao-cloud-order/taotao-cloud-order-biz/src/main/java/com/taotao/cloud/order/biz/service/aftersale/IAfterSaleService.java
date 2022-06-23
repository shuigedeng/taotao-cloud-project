package com.taotao.cloud.order.biz.service.aftersale;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.api.web.dto.aftersale.AfterSaleDTO;
import com.taotao.cloud.order.api.web.query.aftersale.AfterSalePageQuery;
import com.taotao.cloud.order.api.web.vo.aftersale.AfterSaleApplyVO;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.store.api.web.vo.StoreAfterSaleAddressVO;
import com.taotao.cloud.sys.api.web.vo.logistics.TracesVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:10
 */
public interface IAfterSaleService extends IService<AfterSale> {

	/**
	 * 分页查询售后信息
	 *
	 * @param saleSearchParams 查询参数
	 * @return {@link IPage }<{@link AfterSale }>
	 * @since 2022-04-28 08:49:10
	 */
	IPage<AfterSale> getAfterSalePages(AfterSalePageQuery saleSearchParams);

	/**
	 * 查询导出售后信息
	 *
	 * @param saleSearchParams 查询参数
	 * @return {@link List }<{@link AfterSale }>
	 * @since 2022-04-28 08:49:10
	 */
	List<AfterSale> exportAfterSaleOrder(AfterSalePageQuery saleSearchParams);

	/**
	 * 查询售后信息
	 *
	 * @param sn 售后单号
	 * @return {@link AfterSale }
	 * @since 2022-04-28 08:49:10
	 */
	AfterSale getAfterSale(String sn);

	/**
	 * 获取申请售后页面信息
	 *
	 * @param sn 订单编号
	 * @return {@link AfterSaleApplyVO }
	 * @since 2022-04-28 08:49:10
	 */
	AfterSaleApplyVO getAfterSaleVO(String sn);

	/**
	 * 售后申请
	 *
	 * @param afterSaleDTO 售后对象
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:49:10
	 */
	Boolean saveAfterSale(AfterSaleDTO afterSaleDTO);

	/**
	 * 商家审核售后申请
	 *
	 * @param afterSaleSn       售后编号
	 * @param serviceStatus     状态 PASS：审核通过，REFUSE：审核未通过
	 * @param remark            商家备注
	 * @param actualRefundPrice 退款金额
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:49:10
	 */
	Boolean review(String afterSaleSn, String serviceStatus, String remark, BigDecimal actualRefundPrice);

	/**
	 * 买家退货,物流填写
	 *
	 * @param afterSaleSn  售后服务单号
	 * @param logisticsNo  物流单号
	 * @param logisticsId  物流公司ID
	 * @param mDeliverTime 买家退货发货时间
	 * @return {@link AfterSale }
	 * @since 2022-04-28 08:49:10
	 */
	AfterSale buyerDelivery(String afterSaleSn, String logisticsNo, Long logisticsId, LocalDateTime mDeliverTime);

	/**
	 * 获取买家退货物流踪迹
	 *
	 * @param afterSaleSn 售后服务单号
	 * @return {@link TracesVO }
	 * @since 2022-04-28 08:49:10
	 */
	TracesVO deliveryTraces(String afterSaleSn);

	/**
	 * 商家收货
	 *
	 * @param afterSaleSn   售后编号
	 * @param serviceStatus 状态 PASS：审核通过，REFUSE：审核未通过
	 * @param remark        商家备注
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:49:10
	 */
	Boolean storeConfirm(String afterSaleSn, String serviceStatus, String remark);

	/**
	 * 平台退款-线下支付
	 *
	 * @param afterSaleSn 售后单号
	 * @param remark      备注
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:49:10
	 */
	Boolean refund(String afterSaleSn, String remark);

	/**
	 * 买家确认解决问题
	 *
	 * @param afterSaleSn 售后订单sn
	 * @return {@link AfterSale }
	 * @since 2022-04-28 08:49:11
	 */
	AfterSale complete(String afterSaleSn);

	/**
	 * 买家取消售后
	 *
	 * @param afterSaleSn 售后订单sn
	 * @return {@link Boolean }
	 * @since 2022-04-28 08:49:11
	 */
	Boolean cancel(String afterSaleSn);


	/**
	 * 根据售后单号获取店铺退货收货地址信息
	 *
	 * @param sn 售后单号
	 * @return {@link StoreAfterSaleAddressVO }
	 * @since 2022-04-28 08:49:11
	 */
	StoreAfterSaleAddressVO getStoreAfterSaleAddressDTO(String sn);

}
