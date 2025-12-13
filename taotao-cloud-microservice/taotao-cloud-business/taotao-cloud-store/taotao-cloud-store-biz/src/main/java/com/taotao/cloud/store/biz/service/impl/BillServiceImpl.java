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

package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.api.enums.order.FlowTypeEnum;
import com.taotao.cloud.order.api.feign.StoreFlowApi;
import com.taotao.cloud.order.api.model.page.order.StoreFlowQuery;
import com.taotao.cloud.store.api.enums.BillStatusEnum;
import com.taotao.cloud.store.api.model.query.BillPageQuery;
import com.taotao.cloud.store.api.model.vo.BillListVO;
import com.taotao.cloud.store.api.model.vo.StoreDetailInfoVO;
import com.taotao.cloud.store.biz.mapper.BillMapper;
import com.taotao.cloud.store.biz.model.entity.Bill;
import com.taotao.cloud.store.biz.service.IBillService;
import com.taotao.cloud.store.biz.service.IStoreDetailService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结算单业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:02:00
 */
@Service
@CacheConfig(cacheNames = "bill")
@Transactional(rollbackFor = Exception.class)
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    /** 店铺详情 */
    @Autowired
    private IStoreDetailService storeDetailService;
    /** 商家流水 */
    @Autowired
    private StoreFlowApi storeFlowApi;

    @Override
    public void createBill(String storeId, LocalDateTime startTime, LocalDateTime endTime) {
        // 获取结算店铺
        StoreDetailInfoVO store = storeDetailService.getStoreDetailVO(storeId);
        Bill bill = new Bill();

        // 结算基础信息
        bill.setStartTime(startTime);
        // bill.setEndTime(DateUtil.yesterday());
        bill.setBillStatus(BillStatusEnum.OUT.name());
        // bill.setStoreId(storeId);
        bill.setStoreName(store.getStoreName());

        // 设置结算信息
        bill.setBankAccountName(store.getSettlementBankAccountName());
        bill.setBankAccountNumber(store.getSettlementBankAccountNum());
        bill.setBankCode(store.getSettlementBankJointName());
        bill.setBankName(store.getSettlementBankBranchName());

        // 店铺结算单号
        bill.setSn(IdGeneratorUtils.createStr("B"));

        // 入账结算信息
        Bill orderBill = this.baseMapper.getOrderBill(new QueryWrapper<Bill>()
                .eq("store_id", storeId)
                .eq("flow_type", FlowTypeEnum.PAY.name())
                .between("create_time", startTime, endTime));

        BigDecimal orderPrice = BigDecimal.ZERO;
        if (orderBill != null) {
            bill.setOrderPrice(orderBill.getOrderPrice());
            bill.setCommissionPrice(orderBill.getCommissionPrice());
            bill.setDistributionCommission(orderBill.getDistributionCommission());
            bill.setSiteCouponCommission(orderBill.getSiteCouponCommission());
            bill.setPointSettlementPrice(orderBill.getPointSettlementPrice());
            bill.setKanjiaSettlementPrice(orderBill.getKanjiaSettlementPrice());
            // 入账金额=订单金额+积分商品+砍价商品
            orderPrice = CurrencyUtils.add(
                    CurrencyUtils.add(orderBill.getBillPrice(), orderBill.getPointSettlementPrice()),
                    orderBill.getKanjiaSettlementPrice());
        }

        // 退款结算信息
        Bill refundBill = this.baseMapper.getRefundBill(new QueryWrapper<Bill>()
                .eq("store_id", storeId)
                .eq("flow_type", FlowTypeEnum.REFUND.name())
                .between("create_time", startTime, endTime));
        BigDecimal refundPrice = BigDecimal.ZERO;
        if (refundBill != null) {
            bill.setRefundPrice(refundBill.getRefundPrice());
            bill.setRefundCommissionPrice(refundBill.getRefundCommissionPrice());
            bill.setDistributionRefundCommission(refundBill.getDistributionRefundCommission());
            bill.setSiteCouponRefundCommission(refundBill.getSiteCouponRefundCommission());
            refundPrice = refundBill.getBillPrice();
        }

        // 最终结算金额=入款结算金额-退款结算金额
        BigDecimal finalPrice = CurrencyUtils.sub(orderPrice, refundPrice);
        bill.setBillPrice(finalPrice);

        // 添加结算单
        this.save(bill);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void immediatelyBill(String storeId, Long endTime) {

        // Long now = DateUtil.getDateline();
        // // TODO 需要获取真实店铺
        // StoreDetailVO store = new StoreDetailVO();
        // Long startTime = store.getLastBillTime().getTime();
		//
        // store.setLastBillTime(new Date(now));
        // // TODO   store.save 保存新的结束时间
		//
        // // TODO 获取结算周期内的结算详情
        // BillDTO billDTO = new BillDTO();
		//
        // // 如果没有需要结算单，那么就可以直接返回，也不需要保存新的结算单
        // if (billDTO.getOrderPrice() == 0 && billDTO.getRefundPrice() == 0) {
        //     return;
        // }
		//
        // this.createBill(storeId, startTime, endTime);
    }

    @Override
    public IPage<BillListVO> billPage(BillPageQuery billPageQuery) {
        // QueryWrapper<BillListVO> queryWrapper = billPageQuery.queryWrapper();
        // return this.baseMapper.queryBillPage(billPageQuery.buildMpPage(), queryWrapper);
		return null;
    }

    @Override
    public boolean check(String id) {
        Bill bill = this.getById(id);
        // 判断当前结算单状态为：出账
        if (!bill.getBillStatus().equals(BillStatusEnum.OUT.name())) {
            throw new BusinessException(ResultEnum.BILL_CHECK_ERROR);
        }

        // 判断操作人员为商家
        // if (!UserContext.getCurrentUser().getRole().equals(UserEnums.STORE)) {
        // 	throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        // }

        LambdaUpdateWrapper<Bill> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(Bill::getId, id);
        lambdaUpdateWrapper.set(Bill::getBillStatus, BillStatusEnum.CHECK.name());
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public boolean complete(String id) {
        Bill bill = this.getById(id);
        // 判断当前结算单状态为：已核对
        if (!bill.getBillStatus().equals(BillStatusEnum.CHECK.name())) {
            throw new BusinessException(ResultEnum.BILL_COMPLETE_ERROR);
        }

        // 判断操作人员为后台管理员
        // if (!UserContext.getCurrentUser().getRole().equals(UserEnums.MANAGER)) {
        // 	throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        // }

        LambdaUpdateWrapper<Bill> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(Bill::getId, id);
        lambdaUpdateWrapper.set(Bill::getPayTime, new Date());
        lambdaUpdateWrapper.set(Bill::getBillStatus, BillStatusEnum.COMPLETE.name());
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public void download(HttpServletResponse response, String id) {

        Bill bill = this.getById(id);
        ExcelWriter writer = ExcelUtil.getWriterWithSheet("入账订单");
        writer.setSheet("入账订单");
        writer.addHeaderAlias("createTime", "入账时间");
        writer.setColumnWidth(0, 20);
        writer.addHeaderAlias("orderSn", "订单编号");
        writer.setColumnWidth(1, 35);
        writer.addHeaderAlias("storeName", "店铺名称");
        writer.setColumnWidth(2, 20);
        writer.addHeaderAlias("goodsName", "商品名称");
        writer.setColumnWidth(3, 70);
        writer.addHeaderAlias("num", "销售量");
        writer.addHeaderAlias("finalPrice", "订单金额");
        writer.addHeaderAlias("commissionPrice", "平台分佣");
        writer.addHeaderAlias("siteCouponPrice", "平台优惠券");
        writer.setColumnWidth(7, 12);
        writer.addHeaderAlias("distributionRebate", "分销金额");
        writer.addHeaderAlias("pointSettlementPrice", "积分结算金额");
        writer.setColumnWidth(9, 12);
        writer.addHeaderAlias("kanjiaSettlementPrice", "砍价结算金额");
        writer.setColumnWidth(10, 12);
        writer.addHeaderAlias("billPrice", "应结金额");
        writer.setColumnWidth(11, 20);

        StoreFlowQuery.BillDTO billDTO = new StoreFlowQuery.BillDTO();
        BeanUtils.copyProperties(bill, billDTO);
        // List<StoreFlowPayDownloadVO> storeFlowList = storeFlowApi.getStoreFlowPayDownloadVO(StoreFlowQuery.builder()
        //         .type(FlowTypeEnum.PAY.name())
        //         .bill(billDTO)
        //         .build());
        // writer.write(storeFlowList, true);

        writer.setSheet("退款订单");
        writer.addHeaderAlias("createTime", "入账时间");
        writer.setColumnWidth(0, 20);
        writer.addHeaderAlias("orderSn", "订单编号");
        writer.setColumnWidth(1, 35);
        writer.addHeaderAlias("refundSn", "售后单号");
        writer.setColumnWidth(2, 35);
        writer.addHeaderAlias("storeName", "店铺名称");
        writer.setColumnWidth(3, 20);
        writer.addHeaderAlias("goodsName", "商品名称");
        writer.setColumnWidth(4, 70);
        writer.addHeaderAlias("num", "销售量");
        writer.addHeaderAlias("finalPrice", "退款金额");
        writer.addHeaderAlias("commissionPrice", "平台分佣");
        writer.addHeaderAlias("siteCouponPrice", "平台优惠券");
        writer.setColumnWidth(8, 12);
        writer.addHeaderAlias("distributionRebate", "分销金额");
        writer.addHeaderAlias("pointSettlementPrice", "积分结算金额");
        writer.setColumnWidth(10, 12);
        writer.addHeaderAlias("kanjiaSettlementPrice", "砍价结算金额");
        writer.setColumnWidth(11, 12);
        writer.addHeaderAlias("billPrice", "结算金额");
        writer.setColumnWidth(12, 20);

        StoreFlowQuery.BillDTO billDTO1 = new StoreFlowQuery.BillDTO();
        BeanUtils.copyProperties(bill, billDTO1);
        // List<StoreFlowRefundDownloadVO> storeFlowRefundDownloadVOList =
        //         storeFlowApi.getStoreFlowRefundDownloadVO(StoreFlowQuery.builder()
        //                 .type(FlowTypeEnum.REFUND.name())
        //                 .bill(billDTO1)
        //                 .build());
        // writer.write(storeFlowRefundDownloadVOList, true);

        ServletOutputStream out = null;
        try {
            // 设置公共属性，列表名称
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment;filename="
                            + URLEncoder.encode(bill.getStoreName() + "-" + bill.getSn(), StandardCharsets.UTF_8)
                            + ".xls");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (Exception e) {
            log.error("下载结算单错误", e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }
    }
}
