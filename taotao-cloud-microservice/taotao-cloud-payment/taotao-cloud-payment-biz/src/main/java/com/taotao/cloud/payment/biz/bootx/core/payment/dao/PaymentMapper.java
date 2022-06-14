package com.taotao.cloud.payment.biz.bootx.core.payment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**   
* 支付记录
* @author xxm  
* @date 2021/7/27 
*/
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {


}
