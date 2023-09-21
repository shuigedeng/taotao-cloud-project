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

package com.taotao.cloud.wechat.biz.weixin.entity;

import lombok.Data;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @since 2019-05-28 16:12:10
 */
@Data
public class WxMsgVO extends WxMsg {
    private static final long serialVersionUID = 1L;

    /** 数量 */
    private Integer countMsg;

    /** repType not in筛选 */
    private String notInRepType;
}
