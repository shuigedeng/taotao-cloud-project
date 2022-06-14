package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.dao;

import cn.bootx.payment.core.paymodel.wallet.entity.WalletPayment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletPaymentMapper extends BaseMapper<WalletPayment> {
}
