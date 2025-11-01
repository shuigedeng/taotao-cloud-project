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

package com.taotao.cloud.wechat.biz.wecom.core.robot.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.starter.wecom.param.robot.WecomRobotConfigParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Repository
@RequiredArgsConstructor
public class WecomRobotConfigManager extends BaseManager<WecomRobotConfigMapper, WecomRobotConfig> {

    /** 根据code获取机器人配置 */
    public Optional<WecomRobotConfig> findByCode(String code) {
        return findByField(WecomRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(WecomRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return lambdaQuery()
                .eq(WecomRobotConfig::getCode, code)
                .ne(MpIdEntity::getId, id)
                .exists();
    }
    /** 分页 */
    public Page<WecomRobotConfig> page(PageQuery PageQuery, WecomRobotConfigParam param) {
        Page<WecomRobotConfig> mpPage = MpUtil.getMpPage(PageQuery, WecomRobotConfig.class);
        return lambdaQuery()
                .like(StrUtil.isNotBlank(param.getCode()), WecomRobotConfig::getCode, param.getCode())
                .like(StrUtil.isNotBlank(param.getName()), WecomRobotConfig::getCode, param.getName())
                .like(StrUtil.isNotBlank(param.getWebhookKey()), WecomRobotConfig::getWebhookKey, param.getWebhookKey())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
