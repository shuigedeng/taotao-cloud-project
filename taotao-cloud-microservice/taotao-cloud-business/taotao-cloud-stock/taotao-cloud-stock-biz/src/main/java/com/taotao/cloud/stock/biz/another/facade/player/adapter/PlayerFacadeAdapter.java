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

package com.taotao.cloud.stock.biz.another.facade.player.adapter;

import com.taotao.cloud.stock.biz.another.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.stock.biz.another.domain.player.domain.PlayerQueryResultDomain;

public class PlayerFacadeAdapter {

    // domain -> dto
    public PlayerQueryResultDTO convertQuery(PlayerQueryResultDomain domain) {
        if (null == domain) {
            return null;
        }
        PlayerQueryResultDTO result = new PlayerQueryResultDTO();
        result.setPlayerId(domain.getPlayerId());
        result.setPlayerName(domain.getPlayerName());
        result.setHeight(domain.getHeight());
        result.setWeight(domain.getWeight());
        if (null != domain.getGamePerformance()) {
            GamePerformanceDTO performance = convertGamePerformance(domain.getGamePerformance());
            result.setGamePerformanceDTO(performance);
        }
        return result;
    }
}
