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

package com.taotao.cloud.message.biz.channels.websockt.stomp.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.websocket.stomp.processor.WebSocketMessageSender;
import com.taotao.cloud.websocket.stomp.service.WebSocketDisplayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** WebSocket 消息接口 */
@RestController
@RequestMapping("/message/websocket")
@Tags({@Tag(name = "消息接口"), @Tag(name = "WebSocket消息接口")})
public class WebSocketMessageController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageController.class);

    private final WebSocketMessageSender webSocketMessageSender;
    private final WebSocketDisplayService webSocketDisplayService;

    public WebSocketMessageController(
            WebSocketMessageSender webSocketMessageSender, WebSocketDisplayService webSocketDisplayService) {
        this.webSocketMessageSender = webSocketMessageSender;
        this.webSocketDisplayService = webSocketDisplayService;
    }

    @Operation(
            summary = "后端发送通知",
            description = "后端发送 WebSocket 广播通知接口",
            requestBody =
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "是否成功", content = @Content(mediaType = "application/json"))})
    @Parameters({@Parameter(name = "message", required = true, description = "消息实体")})
    @PostMapping("/send/notice")
    public Result<String> sendNotice(@RequestBody String message) {

        if (StringUtils.isNotBlank(message)) {
            webSocketMessageSender.sendNoticeToAll(message);
        }

        return Result.success(message);
    }

    @Operation(summary = "获取统计信息", description = "获取WebSocket相关的统计信息")
    @GetMapping(value = "/stat")
    public Result<Map<String, Object>> findAllStat() {
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> stat = webSocketDisplayService.findAllStat();
        if (MapUtils.isNotEmpty(stat)) {
            return Result.success(stat, 200);
        } else {
            return Result.fail("获取统计信息失败");
        }
    }
}
