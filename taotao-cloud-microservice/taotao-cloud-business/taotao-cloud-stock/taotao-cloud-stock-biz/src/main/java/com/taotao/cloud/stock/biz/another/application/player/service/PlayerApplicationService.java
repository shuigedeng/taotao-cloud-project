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

package com.taotao.cloud.stock.biz.another.application.player.service;

import com.taotao.cloud.ddd.biz.domain.player.service.PlayerDomainService;

// 运动员应用服务
public class PlayerApplicationService {

    @Resource
    private LogDomainService logDomainService;

    @Resource
    private PlayerDomainService playerDomainService;

    @Resource
    private PlayerApplicationAdapter playerApplicationAdapter;

    public boolean updatePlayer(PlayerUpdateAgg agg) {
        // 运动员领域
        boolean result = playerDomainService.updatePlayer(agg.getPlayer());
        // 日志领域
        LogReportDomain logDomain =
                playerApplicationAdapter.convert(agg.getPlayer().getPlayerName());
        logDomainService.log(logDomain);
        return result;
    }
}
