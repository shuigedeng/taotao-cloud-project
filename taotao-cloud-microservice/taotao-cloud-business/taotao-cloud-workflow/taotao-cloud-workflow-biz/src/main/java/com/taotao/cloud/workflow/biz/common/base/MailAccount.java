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

package com.taotao.cloud.workflow.biz.common.base;

import lombok.Data;

/** */
@Data
public class MailAccount {
    /** pop3服务 */
    private String pop3Host;
    /** pop3端口 */
    private int pop3Port;
    /** smtp服务 */
    private String smtpHost;
    /** smtp端口 */
    private int smtpPort;
    /** 账户 */
    private String account;
    /** 账户名称 */
    private String accountName;
    /** 密码 */
    private String password;
    /** SSL */
    private Boolean ssl;
    ;
}
