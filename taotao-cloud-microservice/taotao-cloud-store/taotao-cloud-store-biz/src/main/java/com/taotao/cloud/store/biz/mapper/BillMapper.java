package com.taotao.cloud.store.biz.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.store.api.model.vo.BillListVO;
import com.taotao.cloud.store.biz.model.entity.Bill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 结算单数据处理层
 *
 * @since 2020/11/17 4:27 下午
 */
public interface BillMapper extends BaseSuperMapper<Bill> {

	/**
	 * 查询结算单分页
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 结算单分页
	 */
	@Select("""
		select b.id,b.sn,b.start_time,b.end_time,b.bill_status,b.store_name,b.bill_price,b.create_time 
		from tt_bill as b ${ew.customSqlSegment}
		""")
	IPage<BillListVO> queryBillPage(IPage<BillListVO> page,
									@Param(Constants.WRAPPER) Wrapper<BillListVO> queryWrapper);

	/**
	 * 查询订单结算
	 *
	 * @param queryWrapper 查询条件
	 * @return 结算单
	 */
	@Select("""
		SELECT SUM( final_price ) AS orderPrice,SUM( commission_price ) AS commissionPrice
				,SUM( distribution_rebate ) AS distributionCommission,SUM( site_coupon_commission ) AS siteCouponCommission
				,SUM( point_settlement_price ) AS pointSettlementPrice
				,SUM( kanjia_settlement_price ) AS kanjiaSettlementPrice
				,SUM( bill_price ) AS billPrice
				FROM tt_store_flow ${ew.customSqlSegment}
		""")
	Bill getOrderBill(@Param(Constants.WRAPPER) QueryWrapper<Bill> queryWrapper);

	/**
	 * 查询退款结算单
	 *
	 * @param queryWrapper 查询条件
	 * @return 结算单
	 */
	@Select("""
		SELECT SUM( final_price ) AS refundPrice,SUM( commission_price ) AS refundCommissionPrice
					,SUM( distribution_rebate ) AS distributionRefundCommission,SUM( site_coupon_commission ) AS siteCouponRefundCommission
					,SUM( bill_price ) AS billPrice FROM tt_store_flow ${ew.customSqlSegment}
		""")
	Bill getRefundBill(@Param(Constants.WRAPPER) QueryWrapper<Bill> queryWrapper);
}
