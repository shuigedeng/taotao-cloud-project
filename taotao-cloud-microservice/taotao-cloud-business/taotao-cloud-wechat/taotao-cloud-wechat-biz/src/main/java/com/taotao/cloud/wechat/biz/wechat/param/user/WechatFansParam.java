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

package com.taotao.cloud.wechat.biz.wechat.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信公众号粉丝
 *
 * @author xxm
 * @since 2022-07-16
 */
@Data
@Schema(title = "微信公众号粉丝")
@Accessors(chain = true)
public class WechatFansParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "关联OpenId")
    private String openid;

    @Schema(description = "订阅状态")
    private Boolean subscribeStatus;

    @Schema(description = "订阅时间")
    private LocalDateTime subscribeTime;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "语言")
    private String language;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "备注")
    private String remark;
}
