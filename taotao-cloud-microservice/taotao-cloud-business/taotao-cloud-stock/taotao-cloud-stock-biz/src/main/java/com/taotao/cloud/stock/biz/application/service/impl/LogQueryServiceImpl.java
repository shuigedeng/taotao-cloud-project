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

package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志查询服务实现类
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
@Service
public class LogQueryServiceImpl implements LogQueryService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Page queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<SysLogDO> page = sysLogMapper.selectPage(
                new Query<SysLogDO>().getPage(params),
                new QueryWrapper<SysLogDO>().like(StringUtils.isNotBlank(key), "username", key));
        return PageAssembler.toPage(page);
    }
}
