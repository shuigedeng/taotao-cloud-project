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

package com.taotao.cloud.wechat.biz.wechat.core.user.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wechat.core.user.entity.WechatFans;
import cn.bootx.starter.wechat.param.user.WechatFansParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信公众号粉丝
 *
 * @author xxm
 * @since 2022-07-16
 */
@Repository
@RequiredArgsConstructor
public class WechatFansManager extends BaseManager<WechatFansMapper, WechatFans> {

    /** 分页 */
    public Page<WechatFans> page(PageQuery PageQuery, WechatFansParam param) {
        Page<WechatFans> mpPage = MpUtil.getMpPage(PageQuery, WechatFans.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
    }

    /** 获取最新的一条 */
    public Optional<WechatFans> findLatest() {
        Page<WechatFans> mpPage = new Page<>(0, 1);
        Page<WechatFans> fansPage =
                this.lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
        if (fansPage.getTotal() > 0) {
            return Optional.of(fansPage.getRecords().get(0));
        }
        return Optional.empty();
    }
}
