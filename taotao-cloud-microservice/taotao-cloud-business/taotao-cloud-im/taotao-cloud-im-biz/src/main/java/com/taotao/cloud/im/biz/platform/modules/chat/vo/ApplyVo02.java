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

import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.enums.ApplyStatusEnum;
import com.platform.modules.chat.enums.ApplyTypeEnum;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ApplyVo02 {

    /** 主键 */
    private Long applyId;
    /** 申请状态0无1同意2拒绝 */
    private ApplyStatusEnum applyStatus;
    /** 申请来源 */
    private ApplySourceEnum applySource;
    /** 申请类型1好友2群组 */
    private ApplyTypeEnum applyType;
    /** 理由 */
    private String reason;
    /** 申请时间 */
    private Date createTime;
    /** 用户id */
    private Long userId;
    /** 用户头像 */
    private String portrait;
    /** 用户昵称 */
    private String nickName;

    public String getApplySourceLabel() {
        if (applySource == null) {
            return null;
        }
        return applySource.getInfo();
    }
}
