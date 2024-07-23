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

package com.taotao.cloud.order.application.service.order;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.application.command.cart.dto.clientobject.OrderExportCO;
import com.taotao.cloud.order.application.command.cart.dto.TradeAddCmd.MemberAddressDTO;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderDetailCO;
import com.taotao.cloud.order.application.command.order.dto.OrderPageQry;
import com.taotao.cloud.order.application.command.order.dto.clientobject.OrderSimpleCO;
import com.taotao.cloud.order.application.command.order.dto.clientobject.PaymentLogCO;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderPO;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import zipkin2.storage.Traces;

/**
 * 子订单业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:47
 */
public interface IOrderService extends IService<OrderPO> {

    /**
     * 系统取消订单
     *
     * @param orderSn 订单编号
     * @param reason 错误原因
     * @since 2022-04-28 08:54:47
     */
    void systemCancel(String orderSn, String reason);

    /**
     * 根据sn查询
     *
     * @param orderSn 订单编号
     * @return {@link OrderPO }
     * @since 2022-04-28 08:54:47
     */
    OrderPO getBySn(String orderSn);

    /**
     * 订单查询
     *
     * @param orderPageQry 查询参数
     * @return {@link IPage }<{@link OrderSimpleCO }>
     * @since 2022-04-28 08:54:47
     */
    IPage<OrderSimpleCO> pageQuery(OrderPageQry orderPageQry);

    /**
     * 订单信息
     *
     * @param orderPageQry 查询参数
     * @return {@link List }<{@link OrderPO }>
     * @since 2022-04-28 08:54:47
     */
    List<OrderPO> queryListByParams(OrderPageQry orderPageQry);

    /**
     * 根据促销查询订单
     *
     * @param orderPromotionType 订单类型
     * @param payStatus 支付状态
     * @param parentOrderSn 依赖订单编号
     * @param orderSn 订单编号
     * @return {@link List }<{@link OrderPO }>
     * @since 2022-04-28 08:54:47
     */
    List<OrderPO> queryListByPromotion(String orderPromotionType, String payStatus, String parentOrderSn, String orderSn);

    /**
     * 根据促销查询订单
     *
     * @param orderPromotionType 订单类型
     * @param payStatus 支付状态
     * @param parentOrderSn 依赖订单编号
     * @param orderSn 订单编号
     * @return long
     * @since 2022-04-28 08:54:47
     */
    long queryCountByPromotion(String orderPromotionType, String payStatus, String parentOrderSn, String orderSn);

    /**
     * 父级拼团订单分组
     *
     * @param pintuanId 拼团id
     * @return {@link List }<{@link OrderPO }>
     * @since 2022-04-28 08:54:47
     */
    List<OrderPO> queryListByPromotion(Long pintuanId);

    /**
     * 查询导出订单列表
     *
     * @param orderPageQry 查询参数
     * @return {@link List }<{@link OrderExportCO }>
     * @since 2022-04-28 08:54:47
     */
    List<OrderExportCO> queryExportOrder(OrderPageQry orderPageQry);

    /**
     * 订单详细
     *
     * @param orderSn 订单SN
     * @return {@link OrderDetailCO }
     * @since 2022-04-28 08:54:47
     */
    OrderDetailCO queryDetail(String orderSn);

    /**
     * 创建订单 1.检查交易信息 2.循环交易购物车列表，创建订单以及相关信息
     *
     * @param tradeDTO 交易DTO
     * @return {@link Boolean }
     * @since 2022-04-28 08:54:47
     */
    Boolean intoDB(TradeDTO tradeDTO);

    /**
     * 订单付款 修改订单付款信息 记录订单流水
     *
     * @param orderSn 订单编号
     * @param paymentMethod 支付方法
     * @param receivableNo 第三方流水
     * @since 2022-04-28 08:54:47
     */
    void payOrder(String orderSn, String paymentMethod, String receivableNo);

    /**
     * 订单确认成功
     *
     * @param orderSn
     */
    void afterOrderConfirm(String orderSn);

    /**
     * 取消订单
     *
     * @param orderSn 订单SN
     * @param reason 取消理由
     * @return 订单
     */
    OrderPO cancel(String orderSn, String reason);

    /**
     * 发货信息修改 日志功能内部实现
     *
     * @param orderSn 订单编号
     * @param memberAddressDTO 收货地址信息
     * @return 订单
     */
    OrderPO updateConsignee(String orderSn, MemberAddressDTO memberAddressDTO);

    /**
     * 订单发货
     *
     * @param orderSn 订单编号
     * @param invoiceNumber 发货单号
     * @param logisticsId 物流公司
     * @return 订单
     */
    OrderPO delivery(String orderSn, String invoiceNumber, Long logisticsId);

    /**
     * 获取物流踪迹
     *
     * @param orderSn 订单编号
     * @return 物流踪迹
     */
    Traces getTraces(String orderSn);

    /**
     * 订单核验
     *
     * @param verificationCode 验证码
     * @param orderSn 订单编号
     * @return 订单
     */
    OrderPO take(String orderSn, String verificationCode);

    /**
     * 根据核验码获取订单信息
     *
     * @param verificationCode 验证码
     * @return 订单
     */
    OrderPO getOrderByVerificationCode(String verificationCode);

    /**
     * 订单完成
     *
     * @param orderSn 订单编号
     */
    void complete(String orderSn);

    /**
     * 系统定时完成订单
     *
     * @param orderSn 订单编号
     */
    void systemComplete(String orderSn);

    /**
     * 通过trade 获取订单列表
     *
     * @param tradeSn 交易编号
     * @return 订单列表
     */
    List<OrderPO> getByTradeSn(String tradeSn);

    /**
     * 发送更新订单状态的信息
     *
     * @param orderMessage 订单传输信息
     */
    void sendUpdateStatusMessage(OrderMessage orderMessage);

    /**
     * 根据订单sn逻辑删除订单
     *
     * @param sn 订单sn
     */
    void deleteOrder(String sn);

    /**
     * 开具发票
     *
     * @param sn 订单sn
     * @return 是否成功
     */
    Boolean invoice(String sn);

    /**
     * 自动成团订单处理
     *
     * @param pintuanId 拼团活动id
     * @param parentOrderSn 拼团订单sn
     */
    void agglomeratePintuanOrder(Long pintuanId, String parentOrderSn);

    /**
     * 获取待发货订单编号列表
     *
     * @param response 响应
     * @param logisticsName 店铺已选择物流公司列表
     */
    void downLoadDeliver(HttpServletResponse response, List<String> logisticsName);

    /**
     * 订单批量发货
     *
     * @param files 文件
     * @return
     */
    Boolean batchDeliver(MultipartFile files);

    /**
     * 获取订单实际支付的总金额
     *
     * @param orderSn 订单sn
     * @return 金额
     */
    BigDecimal getPaymentTotal(String orderSn);

    /**
     * 查询订单支付记录
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 订单支付记录分页
     */
    IPage<PaymentLogCO> queryPaymentLogs(IPage<PaymentLogCO> page, Wrapper<PaymentLogCO> queryWrapper);
}
