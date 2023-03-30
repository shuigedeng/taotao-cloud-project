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

package com.taotao.cloud.message.biz.austin.common.constant;

/**
 * Austin常量信息
 *
 * @author 3y
 */
public class AustinConstant {

    /**
     * businessId默认的长度
     * 生成的逻辑：com.taotao.cloud.message.biz.austin.support.utils.TaskInfoUtils#generateBusinessId(java.lang.Long,
     * java.lang.Integer)
     */
    public static final Integer BUSINESS_ID_LENGTH = 16;

    /** 接口限制 最多的人数 */
    public static final Integer BATCH_RECEIVER_SIZE = 100;

    /** 消息发送给全部人的标识 (企业微信 应用消息) (钉钉自定义机器人) (钉钉工作消息) */
    public static final String SEND_ALL = "@all";

    /** 默认的常量，如果新建模板/账号时，没传入则用该常量 */
    public static final String DEFAULT_CREATOR = "Java3y";

    public static final String DEFAULT_UPDATOR = "Java3y";
    public static final String DEFAULT_TEAM = "Java3y公众号";
    public static final String DEFAULT_AUDITOR = "Java3y";
}
