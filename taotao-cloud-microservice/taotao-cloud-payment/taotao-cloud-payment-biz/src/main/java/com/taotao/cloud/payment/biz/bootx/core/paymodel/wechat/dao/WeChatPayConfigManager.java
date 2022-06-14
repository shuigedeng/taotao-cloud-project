package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.entity.WeChatPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**   
* 微信支付配置
* @author xxm  
* @date 2021/3/19 
*/
@Repository
@RequiredArgsConstructor
public class WeChatPayConfigManager extends BaseManager<WeChatPayConfigMapper, WeChatPayConfig> {

    /**
     * 获取启用的支付宝配置
     */
    public Optional<WeChatPayConfig> findEnable(){
        return findByField(WeChatPayConfig::getActivity,Boolean.TRUE);
    }

    public Optional<WeChatPayConfig> findByAppId(String appId) {
        return findByField(WeChatPayConfig::getAppId,appId);
    }

    public void removeAllActivity() {
        lambdaUpdate().eq(WeChatPayConfig::getActivity,Boolean.TRUE)
                .set(WeChatPayConfig::getActivity,Boolean.FALSE);
    }

    public Page<WeChatPayConfig> page(PageParam pageParam) {
        Page<WeChatPayConfig> mpPage = MpUtil.getMpPage(pageParam, WeChatPayConfig.class);
        return lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .page(mpPage);
    }
}
