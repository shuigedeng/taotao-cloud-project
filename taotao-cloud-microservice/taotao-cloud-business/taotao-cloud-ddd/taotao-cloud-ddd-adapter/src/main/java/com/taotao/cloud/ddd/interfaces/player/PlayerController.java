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

package com.taotao.cloud.ddd.interfaces.player;

import com.taotao.cloud.ddd.biz.client.base.result.ResultDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerCreateDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerQueryResultDTO;
import com.taotao.cloud.ddd.biz.client.player.dto.PlayerUpdateDTO;
import com.taotao.cloud.ddd.biz.client.player.service.PlayerClientService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Resource
    private PlayerClientService playerClientService;

    @PostMapping("/add")
    public ResultDTO<Boolean> add(
            @RequestHeader("test-login-info") String loginUserId, @RequestBody PlayerCreateDTO dto) {
        dto.setCreator(loginUserId);
        ResultCommonDTO<Boolean> resultDTO = playerClientService.addPlayer(dto);
        return resultDTO;
    }

    @PostMapping("/update")
    public ResultDTO<Boolean> update(
            @RequestHeader("test-login-info") String loginUserId, @RequestBody PlayerUpdateDTO dto) {
        dto.setUpdator(loginUserId);
        ResultCommonDTO<Boolean> resultDTO = playerClientService.updatePlayer(dto);
        return resultDTO;
    }

    @GetMapping("/{playerId}/query")
    public ResultDTO<PlayerQueryResultDTO> queryById(
            @RequestHeader("test-login-info") String loginUserId, @PathVariable("playerId") String playerId) {
        ResultCommonDTO<PlayerQueryResultDTO> resultDTO = playerClientService.queryById(playerId);
        return resultDTO;
    }
}
