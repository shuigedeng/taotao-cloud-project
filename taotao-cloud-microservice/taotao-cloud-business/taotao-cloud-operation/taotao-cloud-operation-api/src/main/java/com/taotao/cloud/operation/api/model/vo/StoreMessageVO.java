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

package com.taotao.cloud.operation.api.model.vo;

import lombok.Data;

/** 店铺消息表 */
@Data
public class StoreMessageVO {
    /** 关联消息id */
    private String messageId;

    /** 关联店铺id */
    private String storeId;

    /** 关联店铺名称 */
    private String storeName;

    /**
     * 状态 0默认未读 1已读 2回收站
     *
     * @see MessageStatusEnum
     */
    // private String status = MessageStatusEnum.UN_READY.name();

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;
}
