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

package com.taotao.cloud.sys.biz.model.pojo;

import java.io.Serializable;
import lombok.Data;

/** 基础设置 */
@Data
public class BaseSetting implements Serializable {

    private static final long serialVersionUID = -3138023944444671722L;
    /** 站点名称 */
    private String siteName;
    /** icp */
    private String icp;
    /** 后端logo */
    private String domainLogo;
    /** 买家端logo */
    private String buyerSideLogo;
    /** 商家端logo */
    private String storeSideLogo;
    /** 站点地址 */
    private String staticPageAddress;
    /** wap站点地址 */
    private String staticPageWapAddress;
}
