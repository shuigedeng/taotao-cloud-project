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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.modules.wx.entity.MsgReplyRule;
import java.util.List;
import java.util.Map;

public interface MsgReplyRuleService extends IService<MsgReplyRule> {
    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存自动回复规则
     *
     * @param msgReplyRule
     */
    @Override
    boolean save(MsgReplyRule msgReplyRule);

    /**
     * 获取所有的回复规则
     *
     * @return
     */
    List<MsgReplyRule> getRules();

    /**
     * 获取当前时段内所有有效的回复规则
     *
     * @return 有效的规则列表
     */
    List<MsgReplyRule> getValidRules();

    /**
     * 筛选符合条件的回复规则
     *
     * @param appid
     * @param exactMatch 是否精确匹配
     * @param keywords 关键词
     * @return 规则列表
     */
    List<MsgReplyRule> getMatchedRules(String appid, boolean exactMatch, String keywords);
}
