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

package com.taotao.cloud.workflow.biz.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** */
@Slf4j
@Component
public class CacheKeyUtil {

    /** 系统配置 */
    public static final String SYSTEMINFO = "systeminfo";
    /** 系统配置 */
    public static final String WECHATCONFIG = "wechatconfig";
    /** 验证码 */
    public static final String VALIDCODE = "validcode_";
    /** 短信验证码 */
    public static final String SMSVALIDCODE = "sms_validcode_";
    /** 登陆token */
    public static final String LOGINTOKEN = "login_token_";
    /** 登陆在线用户 */
    public static final String LOGINONLINE = "login_online_";
    /** 登陆在线用户 - 移动APP */
    public static final String MOBILELOGINONLINE = "login_online_mobile_";
    /** 移动设备列表 */
    public static final String MOBILEDEVICELIST = "mobiledevicelist";
    /** 用户权限 */
    public static final String USERAUTHORIZE = "authorize_";
    /** 公司选择 */
    public static final String COMPANYSELECT = "companyselect";
    /** 组织选择 */
    public static final String ORGANIZELIST = "organizeList";
    /** 字典数据 */
    public static final String DICTIONARY = "dictionary_";
    /** 远端数据 */
    public static final String DYNAMIC = "dynamic_";
    /** 岗位列表 */
    public static final String POSITIONLIST = "positionlist_";
    /** 所有用户 */
    public static final String ALLUSER = "alluser";
    /** 可视化数据包 */
    public static final String VISIUALDATA = "visiualdata_";
    /** ID生成器 */
    public static String IDGENERATOR = "idgenerator_";

    public String getVisiualData() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + VISIUALDATA;
        }
        return VISIUALDATA;
    }

    public String getCompanySelect() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + COMPANYSELECT;
        }
        return COMPANYSELECT;
    }

    public String getOrganizeList() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + ORGANIZELIST;
        }
        return ORGANIZELIST;
    }

    public String getDictionary() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + DICTIONARY;
        }
        return DICTIONARY;
    }

    public String getDynamic() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + DYNAMIC;
        }
        return DYNAMIC;
    }

    public String getPositionList() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + POSITIONLIST;
        }
        return POSITIONLIST;
    }

    public String getAllUser() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + ALLUSER;
        }
        return ALLUSER;
    }

    public String getSystemInfo() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + SYSTEMINFO;
        }
        return SYSTEMINFO;
    }

    public String getWechatConfig() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + WECHATCONFIG;
        }
        return WECHATCONFIG;
    }

    public String getValidCode() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + VALIDCODE;
        }
        return VALIDCODE;
    }

    public String getSmsValidCode() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + SMSVALIDCODE;
        }
        return SMSVALIDCODE;
    }

    public String getLoginToken(String tenantId) {
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + LOGINTOKEN;
        }
        return LOGINTOKEN;
    }

    public String getLoginOnline() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + LOGINONLINE;
        }
        return LOGINONLINE;
    }

    public String getMobileLoginOnline() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + MOBILELOGINONLINE;
        }
        return MOBILELOGINONLINE;
    }

    public String getMobileDeviceList() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + MOBILEDEVICELIST;
        }
        return MOBILEDEVICELIST;
    }

    /** 用户权限集合 */
    public String getUserAuthorize() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        if (!StringUtil.isEmpty(tenantId)) {
            return tenantId + USERAUTHORIZE;
        }
        return USERAUTHORIZE;
    }
}
