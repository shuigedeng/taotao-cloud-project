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

package com.taotao.cloud.stock.biz.another.facade.player.impl;

import com.taotao.cloud.ddd.biz.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.ddd.biz.client.player.service.PlayerClientService;
import com.taotao.cloud.ddd.biz.domain.player.domain.PlayerQueryResultDomain;
import com.taotao.cloud.ddd.biz.domain.player.service.PlayerDomainService;
import com.taotao.cloud.ddd.biz.facade.player.adapter.PlayerFacadeAdapter;
import jakarta.annotation.Resource;

/** 本层可以引用applicationService，也可以引用domainService，因为对于类似查询等简单业务场景，没有多领域聚合，可以直接使用领域服务。 */
public class PlayerClientServiceImpl implements PlayerClientService {

    @Resource private PlayerDomainService playerDomainService;
    @Resource private PlayerFacadeAdapter playerFacadeAdapter;

    @Override
    public ResultDTO<PlayerQueryResultDTO> queryById(String playerId) {
        PlayerQueryResultDomain resultDomain = playerDomainService.queryPlayerById(playerId);
        if (null == resultDomain) {
            return ResultCommonDTO.success();
        }
        PlayerQueryResultDTO result = playerFacadeAdapter.convertQuery(resultDomain);
        return ResultCommonDTO.success(result);
    }
}
