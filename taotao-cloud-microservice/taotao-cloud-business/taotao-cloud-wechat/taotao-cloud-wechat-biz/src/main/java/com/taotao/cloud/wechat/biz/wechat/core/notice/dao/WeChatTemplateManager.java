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

package com.taotao.cloud.wechat.biz.wechat.core.notice.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wechat.core.notice.entity.WeChatTemplate;
import cn.bootx.starter.wechat.param.notice.WeChatTemplateParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @since 2022/7/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatTemplateManager extends BaseManager<WeChatTemplateMapper, WeChatTemplate> {

    public Page<WeChatTemplate> page(PageQuery PageQuery, WeChatTemplateParam param) {
        Page<WeChatTemplate> mpPage = MpUtil.getMpPage(PageQuery, WeChatTemplate.class);
        return this.lambdaQuery()
                .select(WeChatTemplate.class, MpUtil::excludeBigField)
                .like(StrUtil.isNotBlank(param.getTemplateId()), WeChatTemplate::getTemplateId, param.getTemplateId())
                .like(StrUtil.isNotBlank(param.getName()), WeChatTemplate::getName, param.getName())
                .like(StrUtil.isNotBlank(param.getCode()), WeChatTemplate::getCode, param.getCode())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(WeChatTemplate::getCode, code, id);
    }

    /** 根据code查询 */
    public Optional<WeChatTemplate> findByCode(String code) {
        return this.findByField(WeChatTemplate::getCode, code);
    }

    /** 根据code查询 */
    public Optional<WeChatTemplate> findTemplateIdByCode(String code) {
        return lambdaQuery()
                .select(WeChatTemplate::getTemplateId)
                .eq(WeChatTemplate::getCode, code)
                .oneOpt();
    }
}
