package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.entity.AlipayConfig;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.alipay.AlipayConfigQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
* 支付宝配置
* @author xxm
* @date 2021/2/26
*/
@Repository
@RequiredArgsConstructor
public class AlipayConfigManager extends BaseManager<AlipayConfigMapper, AlipayConfig> {

    /**
     * 获取启用的支付宝配置
     */
    public Optional<AlipayConfig> findActivity(){
        return findByField(AlipayConfig::getActivity,Boolean.TRUE);
    }

    public Page<AlipayConfig> page(PageQuery PageQuery, AlipayConfigQuery param) {
        Page<AlipayConfig> mpPage = MpUtil.getMpPage(PageQuery, AlipayConfig.class);
        return lambdaQuery()
                .select(AlipayConfig.class, MpBigFieldHandler::excludeBigField)
                .like(StrUtil.isNotBlank(param.getName()),AlipayConfig::getName,param.getName())
                .like(StrUtil.isNotBlank(param.getAppId()),AlipayConfig::getAppId,param.getAppId())
                .page(mpPage);
    }

    public void removeAllActivity() {
        lambdaUpdate().eq(AlipayConfig::getActivity,Boolean.TRUE)
                .set(AlipayConfig::getActivity,Boolean.FALSE)
                .update();

    }
}
