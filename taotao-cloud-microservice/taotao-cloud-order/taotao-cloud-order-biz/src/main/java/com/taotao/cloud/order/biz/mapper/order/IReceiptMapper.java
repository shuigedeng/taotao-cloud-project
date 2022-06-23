package com.taotao.cloud.order.biz.mapper.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.api.web.dto.order.OrderReceiptDTO;
import com.taotao.cloud.order.api.web.query.order.ReceiptPageQuery;
import com.taotao.cloud.order.api.web.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.model.entity.order.Receipt;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 发票数据处理层
 */
public interface IReceiptMapper extends BaseMapper<Receipt> {

	/**
	 * 查询发票信息
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 */
	@Select("select r.*,o.order_status from tt_receipt r inner join tt_order o ON o.sn=r.order_sn ${ew.customSqlSegment}")
	IPage<OrderReceiptDTO> getReceipt(IPage<OrderSimpleVO> page,
		@Param(Constants.WRAPPER) Wrapper<ReceiptPageQuery> queryWrapper);
}
