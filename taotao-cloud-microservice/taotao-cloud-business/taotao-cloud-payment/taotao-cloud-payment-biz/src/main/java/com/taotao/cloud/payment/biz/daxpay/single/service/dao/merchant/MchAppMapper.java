package com.taotao.cloud.payment.biz.daxpay.single.service.dao.merchant;

import com.taotao.cloud.payment.biz.daxpay.service.entity.merchant.MchApp;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户应用
 * @author xxm
 * @since 2024/5/27
 */
@Mapper
public interface MchAppMapper extends MPJBaseMapper<MchApp> {
}
