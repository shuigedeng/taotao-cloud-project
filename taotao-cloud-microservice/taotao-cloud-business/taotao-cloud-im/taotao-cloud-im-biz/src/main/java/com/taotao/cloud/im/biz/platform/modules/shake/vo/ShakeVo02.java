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

package com.taotao.cloud.im.biz.platform.modules.shake.vo;

import com.platform.common.enums.GenderEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ShakeVo02 {

    /** 用户ID */
    private Long userId;
    /** 头像 */
    private String portrait;
    /** 昵称 */
    private String nickName;
    /** 性别1男0女 */
    private GenderEnum gender;
    /** 距离 */
    private double distance;
    /** 距离单位 */
    private String distanceUnit;

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }
}
