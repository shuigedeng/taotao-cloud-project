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

package com.taotao.cloud.goods.adapter.model.co.setting;

import java.io.Serializable;
import lombok.Data;

/** OSS设置 */
@Data
public class OssSetting implements Serializable {

    private static final long serialVersionUID = 2975271656230801861L;
    /** 域名 */
    private String endPoint = "";
    /** 储存空间 */
    private String bucketName = "";
    /** 存放路径路径 */
    private String picLocation = "";
    /** 密钥id */
    private String accessKeyId = "";
    /** 密钥 */
    private String accessKeySecret = "";
}
