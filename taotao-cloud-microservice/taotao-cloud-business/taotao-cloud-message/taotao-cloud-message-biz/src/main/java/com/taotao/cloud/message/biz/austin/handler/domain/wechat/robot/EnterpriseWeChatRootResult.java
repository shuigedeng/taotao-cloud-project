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

package com.taotao.cloud.message.biz.austin.handler.domain.wechat.robot;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 企业微信 机器人 返回值
 * https://developer.work.weixin.qq.com/document/path/91770#%E6%96%87%E6%9C%AC%E7%B1%BB%E5%9E%8B
 *
 * @author 3y
 * @date 2022/12/26
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class EnterpriseWeChatRootResult {

    @JSONField(name = "errcode")
    private Integer errcode;

    @JSONField(name = "errmsg")
    private String errmsg;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "media_id")
    private String mediaId;

    @JSONField(name = "created_at")
    private String createdAt;
}
