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

package com.taotao.cloud.sys.adapter.model.co.setting;

import java.io.Serializable;
import lombok.Data;

/** 短信配置 这里在前台不做调整，方便客户直接把服务商的内容配置在我们平台 */
@Data
public class SmsSetting implements Serializable {
    /** 从上到下yi依次是 节点地址 key 密钥 签名 */
    private String regionId;

    private String accessKeyId;

    private String accessSecret;

    private String signName;
}
