package com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.entity.CashPayment;
import org.apache.ibatis.annotations.Mapper;

/**   
* 现金支付
* @author xxm  
* @date 2021/6/23 
*/
@Mapper
public interface CashPaymentMapper extends BaseSuperMapper<CashPayment> {
}
