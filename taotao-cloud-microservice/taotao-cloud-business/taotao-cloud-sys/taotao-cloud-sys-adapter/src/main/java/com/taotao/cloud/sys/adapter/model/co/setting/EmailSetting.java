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

/** 邮箱设置 */
@Data
public class EmailSetting implements Serializable {

    private static final long serialVersionUID = 7261037221941716140L;

    /** 邮箱服务器 */
    private String host;

    /** 发送者邮箱账号 */
    private String username;

    /** 邮箱授权码 */
    private String password;
}
