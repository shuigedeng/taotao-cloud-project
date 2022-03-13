package com.taotao.cloud.order.biz.mapper.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.biz.entity.order.StoreFlow;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商家订单流水数据处理层
 */
public interface StoreFlowMapper extends BaseMapper<StoreFlow> {

	/**
	 * 获取结算单的入账流水
	 *
	 * @param queryWrapper 查询条件
	 * @return 入账流水
	 */
	@Select("SELECT * FROM li_store_flow ${ew.customSqlSegment}")
	List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(
		@Param(Constants.WRAPPER) Wrapper<StoreFlow> queryWrapper);

	/**
	 * 获取结算单的退款流水
	 *
	 * @param queryWrapper 查询条件
	 * @return 退款流水
	 */
	@Select("SELECT * FROM li_store_flow ${ew.customSqlSegment}")
	List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(
		@Param(Constants.WRAPPER) Wrapper<StoreFlow> queryWrapper);
}
