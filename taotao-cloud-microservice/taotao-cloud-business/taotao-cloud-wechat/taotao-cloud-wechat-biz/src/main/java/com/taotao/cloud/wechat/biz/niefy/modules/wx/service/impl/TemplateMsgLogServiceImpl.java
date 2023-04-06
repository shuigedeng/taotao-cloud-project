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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.Query;
import com.github.niefy.modules.wx.dao.TemplateMsgLogMapper;
import com.github.niefy.modules.wx.entity.TemplateMsgLog;
import com.github.niefy.modules.wx.service.TemplateMsgLogService;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TemplateMsgLogServiceImpl extends ServiceImpl<TemplateMsgLogMapper, TemplateMsgLog>
        implements TemplateMsgLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String appid = (String) params.get("appid");
        IPage<TemplateMsgLog> page = this.page(
                new Query<TemplateMsgLog>().getPage(params),
                new QueryWrapper<TemplateMsgLog>().eq(StringUtils.hasText(appid), "appid", appid));

        return new PageUtils(page);
    }

    /**
     * 记录log，异步入库
     *
     * @param log
     */
    @Override
    @Async
    public void addLog(TemplateMsgLog log) {
        this.baseMapper.insert(log);
    }
}
