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

package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.biz.model.entity.MemberNoticeSenter;

/** 会员消息业务层 */
public interface IMemberNoticeSenterService extends IService<MemberNoticeSenter> {

    /**
     * 自定义保存方法
     *
     * @param memberNoticeSenter 会员消息
     * @return 操作状态
     */
    boolean customSave(MemberNoticeSenter memberNoticeSenter);
}
