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

package com.taotao.cloud.order.biz.service.business.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.sys.model.page.distribution.DistributionPageQuery;
import com.taotao.cloud.order.sys.model.page.order.StoreFlowPageQuery;
import com.taotao.cloud.order.sys.model.page.store.StorePageQuery;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSale;
import com.taotao.cloud.order.biz.model.entity.order.StoreFlow;
import com.taotao.cloud.store.api.model.vo.StoreFlowPayDownloadVO;
import com.taotao.cloud.store.api.model.vo.StoreFlowRefundDownloadVO;
import java.util.List;

/**
 * 商家订单流水业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:54:53
 */
public interface StoreFlowService extends IService<StoreFlow> {

    /**
     * 支付订单
     *
     * @param orderSn 订单编号
     * @since 2022-04-28 08:54:53
     */
    void payOrder(String orderSn);

    /**
     * 订单退款
     *
     * @param afterSale 售后单
     * @since 2022-04-28 08:54:53
     */
    void refundOrder(AfterSale afterSale);

    /**
     * 获取商家流水
     *
     * @param storeFlowQueryDTO 查询参数
     * @return {@link IPage }<{@link StoreFlow }>
     * @since 2022-04-28 08:54:53
     */
    IPage<StoreFlow> getStoreFlow(StoreFlowPageQuery storeFlowQueryDTO);

    /**
     * 根据参数查询一条数据
     *
     * @param storeFlowQueryDTO 查询参数
     * @return {@link StoreFlow }
     * @since 2022-04-28 08:54:53
     */
    StoreFlow queryOne(StoreFlowPageQuery storeFlowQueryDTO);

    /**
     * 获取结算单地入账流水
     *
     * @param storeFlowQueryDTO 查询条件
     * @return {@link List }<{@link StoreFlowPayDownloadVO }>
     * @since 2022-04-28 08:54:53
     */
    List<StoreFlowPayDownloadVO> getStoreFlowPayDownloadVO(StoreFlowPageQuery storeFlowQueryDTO);

    /**
     * 获取结算单的退款流水
     *
     * @param storeFlowQueryDTO 查询条件
     * @return {@link List }<{@link StoreFlowRefundDownloadVO }>
     * @since 2022-04-28 08:54:53
     */
    List<StoreFlowRefundDownloadVO> getStoreFlowRefundDownloadVO(StoreFlowPageQuery storeFlowQueryDTO);

    /**
     * 根据结算单ID获取商家流水
     *
     * @param storePageQuery 存储页面查询
     * @return {@link IPage }<{@link StoreFlow }>
     * @since 2022-05-19 15:47:59
     */
    IPage<StoreFlow> getStoreFlow(StorePageQuery storePageQuery);

    /**
     * 根据结算单ID获取商家流水
     *
     * @param distributionPageQuery 分配页面查询
     * @return {@link IPage }<{@link StoreFlow }>
     * @since 2022-05-19 15:48:02
     */
    IPage<StoreFlow> getDistributionFlow(DistributionPageQuery distributionPageQuery);

    /**
     * 获取店铺流水
     *
     * @param storeFlowQueryDTO 店铺流水查询参数
     * @return {@link List }<{@link StoreFlow }>
     * @since 2022-04-28 08:54:53
     */
    List<StoreFlow> listStoreFlow(StoreFlowPageQuery storeFlowQueryDTO);
}
