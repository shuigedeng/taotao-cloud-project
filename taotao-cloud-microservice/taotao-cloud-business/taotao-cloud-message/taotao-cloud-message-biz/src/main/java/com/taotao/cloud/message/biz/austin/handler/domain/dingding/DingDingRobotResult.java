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

package com.taotao.cloud.message.biz.austin.handler.domain.dingding;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉群 自定义机器人返回的结果
 *
 * <p>正常的返回：{"errcode":0,"errmsg":"ok"}
 *
 * @author 3y
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class DingDingRobotResult {

    /** errcode */
    @SerializedName("errcode")
    private Integer errCode;

    /** errmsg */
    @SerializedName("errmsg")
    private String errMsg;
}
