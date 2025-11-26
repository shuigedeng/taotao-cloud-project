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

package com.taotao.cloud.workflow.biz.common.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.*;

/** */
@Data
public class TenantPasswordModel {
    /** 手机号 */
    @JSONField(name = "Mobile")
    private String mobile;
    /** 短信验证码 */
    @JSONField(name = "SmsCode")
    private String smsCode;
    /** 密码 */
    @JSONField(name = "Password")
    private String password;
    /** 公司名 */
    @JSONField(name = "CompanyName")
    private String companyName;
    /** 姓名 */
    @JSONField(name = "Name")
    private String name;
}
