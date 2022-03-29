/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package com.taotao.cloud.common.constant;

/**
 * 规则模块的常量
 */
public final class RuleConstants {

    /**
     * 用户端操作异常的错误码分类编号
     */
    public static final String USER_OPERATION_ERROR_TYPE_CODE = "A";

    /**
     * 业务执行异常的错误码分类编号
     */
    public static final String BUSINESS_ERROR_TYPE_CODE = "B";

    /**
     * 第三方调用异常的错误码分类编号
     */
    public static final  String THIRD_ERROR_TYPE_CODE = "C";

    /**
     * 一级宏观码标识，宏观码标识代表一类错误码的统称
     */
    public static final String FIRST_LEVEL_WIDE_CODE = "0001";

    /**
     * 请求成功的返回码
     */
    public static final String SUCCESS_CODE = "00000";

    /**
     * 请求成功的返回信息
     */
    public static final String SUCCESS_MESSAGE = "请求成功";

    /**
     * 规则模块的名称
     */
    public static final String RULE_MODULE_NAME = "kernel-a-rule";

    /**
     * 异常枚举的步进值
     */
    public static final String RULE_EXCEPTION_STEP_CODE = "01";

    /**
     * 一级公司的父级id
     */
    public static final Long TREE_ROOT_ID = -1L;

    /**
     * 中文的多语言类型编码
     */
    public static final String CHINESE_TRAN_LANGUAGE_CODE = "chinese";

    /**
     * 租户数据源标识前缀
     */
    public static final String TENANT_DB_PREFIX = "sys_tenant_db_";

    /**
     * base64图片前缀，用在给<img src=""/>使用
     */
    public static final String BASE64_IMG_PREFIX = "data:image/png;base64,";

    /**
     * 系统配置初始化的标识的常量名称，用在sys_config表作为config_code
     */
    public static final String SYSTEM_CONFIG_INIT_FLAG_NAME = "SYS_CONFIG_INIT_FLAG";

}
