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

package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.enums.FriendTypeEnum;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import lombok.experimental.*;

/** 好友详情 */
@Data
@Accessors(chain = true) // 链式调用
public class FriendVo07 {

    /** 用户id */
    private Long userId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String portrait;
    /** 性别1男0女 */
    private GenderEnum gender;
    /** 封面 */
    private String cover;
    /** 微聊号 */
    private String chatNo;
    /** 省份 */
    private String provinces;
    /** 城市 */
    private String city;
    /** 介绍 */
    private String intro;
    /** 是否是好友 */
    private YesOrNoEnum isFriend = YesOrNoEnum.NO;
    /** 是否黑名单 */
    private YesOrNoEnum black = YesOrNoEnum.NO;
    /** 好友来源 */
    private ApplySourceEnum source;
    /** 好友类型 */
    private FriendTypeEnum userType = FriendTypeEnum.NORMAL;

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }

    public String getSourceLabel() {
        if (source == null) {
            return null;
        }
        return source.getInfo();
    }
}
