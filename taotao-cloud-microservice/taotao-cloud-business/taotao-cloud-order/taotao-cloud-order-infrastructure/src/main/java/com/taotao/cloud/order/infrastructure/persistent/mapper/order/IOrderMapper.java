/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.infrastructure.persistent.mapper.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderPO;
import com.taotao.cloud.order.sys.model.vo.cart.OrderExportVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.sys.model.vo.order.PaymentLogVO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/** 订单数据处理层 */
public interface IOrderMapper extends BaseSuperMapper<OrderPO, Long> {

    /**
     * 修改订单状态
     *
     * @param status 状态
     * @param orderSn 订单编号
     */
    @Update({"update tt_order set order_status = #{status} where sn = #{orderSn}"})
    void updateStatus(String status, String orderSn);

    /**
     * 查询导出订单DTO列表
     *
     * @param queryWrapper 查询条件
     * @return 导出订单DTO列表
     */
    @Select(
            """
		SELECT o.sn,o.create_time,o.member_name,o.consignee_name,o.consignee_mobile,o.consignee_address_path,o.consignee_detail,
			   o.payment_method, o.logistics_name,o.freight_price,o.goods_price,o.discount_price,o.flow_price,oi.goods_name,oi.num,
			   o.remark,o.order_status,o.pay_status,o.deliver_status,o.need_receipt,o.store_name
		FROM tt_order o LEFT JOIN tt_order_item oi ON oi.order_sn=o.sn
		${ew.customSqlSegment}
		""")
    List<OrderExportVO> queryExportOrder(@Param(Constants.WRAPPER) Wrapper<OrderSimpleVO> queryWrapper);

    /**
     * 查询订单支付记录
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 订单支付记录分页
     */
    @Select("select * from tt_order ${ew.customSqlSegment} ")
    IPage<PaymentLogVO> queryPaymentLogs(
            IPage<PaymentLogVO> page, @Param(Constants.WRAPPER) Wrapper<PaymentLogVO> queryWrapper);

    /**
     * 查询订单简短信息分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 简短订单分页
     */
    @Select(
            """
		select o.sn,o.flow_price,o.create_time,o.order_status,o.pay_status,o.payment_method,o.payment_time,o.member_name,o.store_name as store_name,o.store_id as store_id,o.client_type,o.order_type,o.deliver_status,
				GROUP_CONCAT(oi.goods_id) as group_goods_id,
				GROUP_CONCAT(oi.sku_id) as group_sku_id,
				GROUP_CONCAT(oi.num) as group_num,
				GROUP_CONCAT(oi.image) as group_images,
				GROUP_CONCAT(oi.goods_name) as group_name,
				GROUP_CONCAT(oi.after_sale_status) as group_after_sale_status,
				GROUP_CONCAT(oi.complain_status) as group_complain_status,
				GROUP_CONCAT(oi.comment_status) as group_comment_status,
				GROUP_CONCAT(oi.sn) as group_order_items_sn,
				GROUP_CONCAT(oi.goods_price) as group_goods_price
		FROM tt_order o LEFT JOIN tt_order_item AS oi on o.sn = oi.order_sn
		${ew.customSqlSegment}
		""")
    IPage<OrderSimpleVO> queryByParams(
            IPage<OrderSimpleVO> page, @Param(Constants.WRAPPER) Wrapper<OrderSimpleVO> queryWrapper);

    /**
     * 查询订单信息
     *
     * @param queryWrapper 查询条件
     * @return 简短订单分页
     */
    @Select(
            """
		select o.*
		FROM tt_order o INNER JOIN tt_order_item AS oi on o.sn = oi.order_sn
		${ew.customSqlSegment}
		""")
    List<OrderPO> queryListByParams(@Param(Constants.WRAPPER) Wrapper<OrderPO> queryWrapper);
}
