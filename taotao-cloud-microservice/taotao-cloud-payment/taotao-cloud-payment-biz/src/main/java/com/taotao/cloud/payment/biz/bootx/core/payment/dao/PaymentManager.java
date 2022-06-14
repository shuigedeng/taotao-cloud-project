package com.taotao.cloud.payment.biz.bootx.core.payment.dao;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.param.payment.PaymentQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PaymentManager extends BaseManager<PaymentMapper, Payment> {

    /**
     * 按业务ID顺序按创建时间Desc查找非取消的支付单
     */
    public List<Payment> findByBusinessIdNoCancelDesc(String businessId) {
        // "FROM Payment WHERE businessId = ?1 AND payStatus  ?2 order by createTime description"

        return lambdaQuery()
                .eq(Payment::getBusinessId,businessId)
                .notIn(Payment::getPayStatus, PayStatusCode.TRADE_CANCEL)
                .orderByDesc(Payment::getId)
                .list();
    }

    /**
     * 按业务ID顺序按创建时间Desc查找的支付单
     */
    public List<Payment> findByBusinessIdDesc(String businessId) {
        // "FROM Payment WHERE businessId = ?1  order by createTime description"
        return lambdaQuery()
                .eq(Payment::getBusinessId,businessId)
                .orderByDesc(Payment::getId)
                .list();
    }

    /**
     * 根据用户查询
     */
    public List<Payment> findByUserId(Long userId){
        return this.findAllByField(Payment::getUserId,userId);
    }

    /**
     * 分页查询
     */
    public Page<Payment> page(PageParam pageParam, PaymentQuery param, OrderParam orderParam) {
        Page<Payment> mpPage = MpUtil.getMpPage(pageParam, Payment.class);
        return query()
                .orderBy(Objects.nonNull(orderParam.getSortField()),orderParam.isAsc(), StrUtil.toUnderlineCase(orderParam.getSortField()))
                .like(Objects.nonNull(param.getPaymentId()), MpUtil.getColumnName(Payment::getId),param.getPaymentId())
                .like(Objects.nonNull(param.getBusinessId()),MpUtil.getColumnName(Payment::getBusinessId),param.getBusinessId())
                .like(Objects.nonNull(param.getTitle()),MpUtil.getColumnName(Payment::getTitle),param.getTitle())
                .page(mpPage);
    }
    /**
     * 分页查询
     */
    public Page<Payment> superPage(PageParam pageParam, QueryParams queryParams) {
        QueryWrapper<Payment> wrapper = QueryGenerator.generator(queryParams);
        Page<Payment> mpPage = MpUtil.getMpPage(pageParam, Payment.class);
        return this.page(mpPage,wrapper);
    }

}
