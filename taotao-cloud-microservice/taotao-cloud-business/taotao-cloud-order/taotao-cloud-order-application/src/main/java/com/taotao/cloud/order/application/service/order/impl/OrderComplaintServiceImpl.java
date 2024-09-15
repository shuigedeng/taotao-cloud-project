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

package com.taotao.cloud.order.application.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.order.application.service.order.IOrderComplaintService;
import com.taotao.cloud.order.application.service.order.IOrderItemService;
import com.taotao.cloud.order.application.service.order.IOrderService;
import com.taotao.cloud.order.infrastructure.persistent.mapper.order.IOrderComplaintMapper;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderComplaintPO;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 交易投诉业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:55:04
 */
@AllArgsConstructor
@Service
public class OrderComplaintServiceImpl extends ServiceImpl<IOrderComplaintMapper, OrderComplaintPO>
        implements IOrderComplaintService {

    /** 订单 */
    private final IOrderService orderService;
    /** 订单货物 */
    private final IOrderItemService orderItemService;
    /** 商品规格 */
    private final IFeignGoodsSkuApi goodsSkuApi;
    /** 交易投诉沟通 */
    private final IOrderComplaintCommunicationService orderComplaintCommunicationService;

    @Override
    public IPage<OrderComplaintPO> pageQuery(OrderComplaintPageQuery pageQuery) {
        LambdaQueryWrapper<OrderComplaintPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getStatus()), OrderComplaintPO::getComplainStatus, pageQuery.getStatus());
        queryWrapper.eq(StrUtil.isNotEmpty(pageQuery.getOrderSn()), OrderComplaintPO::getOrderSn, pageQuery.getOrderSn());
        queryWrapper.like(
                StrUtil.isNotEmpty(pageQuery.getStoreName()), OrderComplaintPO::getStoreName, pageQuery.getStoreName());
        queryWrapper.eq(StrUtil.isNotEmpty(pageQuery.getStoreId()), OrderComplaintPO::getStoreId, pageQuery.getStoreId());
        queryWrapper.like(
                StrUtil.isNotEmpty(pageQuery.getMemberName()),
                OrderComplaintPO::getMemberName,
                pageQuery.getMemberName());
        queryWrapper.eq(
                StrUtil.isNotEmpty(pageQuery.getMemberId()), OrderComplaintPO::getMemberId, pageQuery.getMemberId());
        queryWrapper.eq(OrderComplaintPO::getDelFlag, false);
        queryWrapper.orderByDesc(OrderComplaintPO::getCreateTime);

        return this.page(pageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public OrderComplaintVO getOrderComplainById(Long id) {
        OrderComplaintPO orderComplaintPO = this.checkOrderComplainExist(id);
        LambdaQueryWrapper<OrderComplaintCommunication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComplaintCommunication::getComplainId, id);
        List<OrderComplaintCommunication> list = orderComplaintCommunicationService.list(queryWrapper);
        OrderComplaintVO orderComplainVO = new OrderComplaintVO(orderComplaintPO);
        orderComplainVO.setOrderComplaintCommunications(list);
        orderComplainVO.setOrderComplaintImages(orderComplaintPO.getImages().split(","));
        if (orderComplaintPO.getAppealImages() != null) {
            orderComplainVO.setAppealImagesList(orderComplaintPO.getAppealImages().split(","));
        }
        return orderComplainVO;
    }

    @Override
    public OrderComplaintPO getOrderComplainByStoreId(Long storeId) {
        return this.getOne(new LambdaQueryWrapper<OrderComplaintPO>().eq(OrderComplaintPO::getStoreId, storeId));
    }

    @Override
    public OrderComplaintPO addOrderComplain(OrderComplaintDTO orderComplaintDTO) {
        try {
            SecurityUser currentUser = SecurityUtils.getCurrentUser();
            // 查询订单信息
            OrderDetailVO orderDetailVO = orderService.queryDetail(orderComplaintDTO.getOrderSn());
            List<OrderItemVO> orderItems = orderDetailVO.getOrderItems();
            OrderItemVO orderItem = orderItems.stream()
                    .filter(i -> orderComplaintDTO.getSkuId().equals(i.getSkuId()))
                    .findFirst()
                    .orElse(null);

            if (orderItem == null) {
                throw new BusinessException(ResultEnum.COMPLAINT_ORDER_ITEM_EMPTY_ERROR);
            }

            // 新建交易投诉
            OrderComplaintPO orderComplaintPO = new OrderComplaintPO();
            BeanUtils.copyProperties(orderComplaintDTO, orderComplaintPO);

            // 获取商品规格信息
            GoodsSkuSpecGalleryVO goodsSku = goodsSkuApi.getGoodsSkuByIdFromCache(orderItem.getSkuId());
            if (goodsSku == null) {
                throw new BusinessException(ResultEnum.COMPLAINT_SKU_EMPTY_ERROR);
            }
            orderComplaintPO.setComplainStatus(ComplaintStatusEnum.NEW.name());
            orderComplaintPO.setGoodsId(goodsSku.getGoodsId());
            orderComplaintPO.setGoodsName(goodsSku.getGoodsName());
            orderComplaintPO.setGoodsImage(goodsSku.getThumbnail());
            orderComplaintPO.setGoodsPrice(goodsSku.getPrice());
            orderComplaintPO.setNum(orderItem.getNum());

            // 获取订单信息
            orderComplaintPO.setOrderTime(orderDetailVO.getOrder().getcre());
            orderComplaintPO.setOrderPrice(
                    orderDetailVO.getOrder().getPriceDetailDTO().getBillPrice());
            orderComplaintPO.setNum(orderDetailVO.getOrder().getGoodsNum());
            orderComplaintPO.setFreightPrice(
                    orderDetailVO.getOrder().getPriceDetailDTO().getFreightPrice());
            orderComplaintPO.setLogisticsNo(orderDetailVO.getOrder().getLogisticsNo());
            orderComplaintPO.setConsigneeMobile(orderDetailVO.getOrder().getConsigneeMobile());
            orderComplaintPO.setConsigneeAddressPath(orderDetailVO.getOrder().getConsigneeAddressPath());
            orderComplaintPO.setConsigneeName(orderDetailVO.getOrder().getConsigneeName());

            // 获取商家信息
            orderComplaintPO.setStoreId(orderDetailVO.getOrder().getStoreId());
            orderComplaintPO.setStoreName(orderDetailVO.getOrder().getStoreName());

            orderComplaintPO.setMemberId(currentUser.getUserId());
            orderComplaintPO.setMemberName(currentUser.getUsername());
            // 保存订单投诉
            this.save(orderComplaintPO);

            // 更新订单投诉状态
            orderItemService.updateOrderItemsComplainStatus(
                    orderComplaintPO.getOrderSn(),
                    orderComplaintPO.getSkuId(),
                    orderComplaintPO.getId(),
                    OrderComplaintStatusEnum.APPLYING);
            return orderComplaintPO;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("订单投诉异常：", e);
            throw new BusinessException(ResultEnum.COMPLAINT_ERROR);
        }
    }

    @Override
    public Boolean updateOrderComplain(OrderComplaintPO orderComplaintPO) {
        OperationalJudgment.judgment(this.checkOrderComplainExist(orderComplaintPO.getId()));
        return this.updateById(orderComplaintPO);
    }

    @Override
    public Boolean updateOrderComplainByStatus(OrderComplaintOperationDTO orderComplaintOperationDTO) {
        OrderComplaintPO orderComplaintPO =
                OperationalJudgment.judgment(this.checkOrderComplainExist(orderComplaintOperationDTO.getComplainId()));
        this.checkOperationParams(orderComplaintOperationDTO, orderComplaintPO);
        orderComplaintPO.setComplainStatus(orderComplaintOperationDTO.getComplainStatus());
        this.updateById(orderComplaintPO);
        return true;
    }

    @Override
    public long waitComplainNum() {
        QueryWrapper<OrderComplaintPO> queryWrapper = Wrappers.query();
        queryWrapper.ne("complain_status", ComplaintStatusEnum.COMPLETE.name());
        queryWrapper.eq(
                CharSequenceUtil.equals(UserContext.getCurrentUser().getRole().name(), UserEnums.STORE.name()),
                "store_id",
                UserContext.getCurrentUser().getStoreId());
        return this.count(queryWrapper);
    }

    @Override
    public Boolean cancel(Long id) {
        OrderComplaintPO orderComplaintPO = OperationalJudgment.judgment(this.getById(id));
        // 如果以及仲裁，则不可以进行申诉取消
        if (orderComplaintPO.getComplainStatus().equals(ComplaintStatusEnum.COMPLETE.name())) {
            throw new BusinessException(ResultEnum.COMPLAINT_CANCEL_ERROR);
        }
        LambdaUpdateWrapper<OrderComplaintPO> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(OrderComplaintPO::getId, id);
        lambdaUpdateWrapper.set(OrderComplaintPO::getComplainStatus, ComplaintStatusEnum.CANCEL.name());
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public Boolean appeal(StoreAppealDTO storeAppealDTO) {
        // 获取投诉信息
        OrderComplaintPO orderComplaintPO =
                OperationalJudgment.judgment(this.checkOrderComplainExist(storeAppealDTO.getOrderComplaintId()));
        orderComplaintPO.setAppealContent(storeAppealDTO.getAppealContent());
        orderComplaintPO.setAppealImages(storeAppealDTO.getAppealImages());
        orderComplaintPO.setAppealTime(LocalDateTime.now());
        orderComplaintPO.setComplainStatus(ComplaintStatusEnum.WAIT_ARBITRATION.name());
        this.updateById(orderComplaintPO);
        return true;
    }

    private OrderComplaintPO checkOrderComplainExist(Long id) {
        OrderComplaintPO orderComplaintPO = this.getById(id);
        if (orderComplaintPO == null) {
            throw new BusinessException(ResultEnum.COMPLAINT_NOT_EXIT);
        }
        return orderComplaintPO;
    }

    private void checkOperationParams(
            OrderComplaintOperationDTO orderComplaintOperationDTO, OrderComplaintPO orderComplaintPO) {
        ComplaintStatusEnum complaintStatusEnum =
                ComplaintStatusEnum.valueOf(orderComplaintOperationDTO.getComplainStatus());
        if (complaintStatusEnum == ComplaintStatusEnum.COMPLETE) {
            if (CharSequenceUtil.isEmpty(orderComplaintOperationDTO.getArbitrationResult())) {
                throw new BusinessException(ResultEnum.COMPLAINT_ARBITRATION_RESULT_ERROR);
            }
            orderComplaintPO.setArbitrationResult(orderComplaintOperationDTO.getArbitrationResult());
        } else if (complaintStatusEnum == ComplaintStatusEnum.COMMUNICATION) {
            if (CharSequenceUtil.isEmpty(orderComplaintOperationDTO.getAppealContent())
                    || orderComplaintOperationDTO.getImages() == null) {
                throw new BusinessException(ResultEnum.COMPLAINT_APPEAL_CONTENT_ERROR);
            }
            orderComplaintPO.setContent(orderComplaintOperationDTO.getAppealContent());
            orderComplaintPO.setImages(orderComplaintOperationDTO.getImages().get(0));
        }
    }
}
