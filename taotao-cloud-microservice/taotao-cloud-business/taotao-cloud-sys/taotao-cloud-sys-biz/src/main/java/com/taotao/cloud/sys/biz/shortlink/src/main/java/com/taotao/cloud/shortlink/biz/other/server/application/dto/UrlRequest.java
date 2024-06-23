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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other.server.application.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UrlRequest {

    @ApiModelProperty("单次请求的id")
    private String requestId;

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("长域名地址")
    private String longUrl;

    @ApiModelProperty("短域名地址")
    private String shortUrl;
}
