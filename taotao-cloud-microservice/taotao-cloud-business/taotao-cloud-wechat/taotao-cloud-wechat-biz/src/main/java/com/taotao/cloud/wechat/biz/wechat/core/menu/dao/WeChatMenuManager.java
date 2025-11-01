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

package com.taotao.cloud.wechat.biz.wechat.core.menu.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wechat.core.menu.entity.WeChatMenu;
import cn.bootx.starter.wechat.param.menu.WeChatMenuParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@Repository
@RequiredArgsConstructor
public class WeChatMenuManager extends BaseManager<WeChatMenuMapper, WeChatMenu> {

    /** 分页 */
    public Page<WeChatMenu> page(PageQuery PageQuery, WeChatMenuParam param) {
        Page<WeChatMenu> mpPage = MpUtil.getMpPage(PageQuery, WeChatMenu.class);
        return lambdaQuery()
                .select(this.getEntityClass(), MpUtil::excludeBigField)
                .eq(StrUtil.isNotBlank(param.getName()), WeChatMenu::getName, param.getName())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }

    /** 清除其他发布状态 */
    public void clearPublish() {
        lambdaUpdate()
                .eq(WeChatMenu::isPublish, true)
                .set(WeChatMenu::isPublish, false)
                .update();
    }
}
