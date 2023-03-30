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

package com.taotao.cloud.payment.biz.kit.plugin.wechat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/** 统一下单-H5 场景信息 */
@Data
@Accessors(chain = true)
public class H5Info {
    /** 场景类型 */
    private String type;
    /** 应用名称 */
    private String app_name;
    /** 网站URL */
    private String app_url;
    /** iOS 平台 BundleID */
    private String bundle_id;
    /** Android 平台 PackageName */
    private String package_name;
}
