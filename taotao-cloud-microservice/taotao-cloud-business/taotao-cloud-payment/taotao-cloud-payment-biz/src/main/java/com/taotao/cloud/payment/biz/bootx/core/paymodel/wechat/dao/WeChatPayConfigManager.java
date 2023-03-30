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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.entity.WeChatPayConfig;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信支付配置
 *
 * @author xxm
 * @date 2021/3/19
 */
@Repository
@RequiredArgsConstructor
public class WeChatPayConfigManager extends BaseManager<WeChatPayConfigMapper, WeChatPayConfig> {

    /** 获取启用的支付宝配置 */
    public Optional<WeChatPayConfig> findEnable() {
        return findByField(WeChatPayConfig::getActivity, Boolean.TRUE);
    }

    public Optional<WeChatPayConfig> findByAppId(String appId) {
        return findByField(WeChatPayConfig::getAppId, appId);
    }

    public void removeAllActivity() {
        lambdaUpdate()
                .eq(WeChatPayConfig::getActivity, Boolean.TRUE)
                .set(WeChatPayConfig::getActivity, Boolean.FALSE);
    }

    public Page<WeChatPayConfig> page(PageQuery PageQuery) {
        Page<WeChatPayConfig> mpPage = MpUtil.getMpPage(PageQuery, WeChatPayConfig.class);
        return lambdaQuery().orderByDesc(MpBaseEntity::getId).page(mpPage);
    }
}
