package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.dao;

import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeChatPaymentMapper extends BaseSuperMapper<WeChatPayment> {

}
