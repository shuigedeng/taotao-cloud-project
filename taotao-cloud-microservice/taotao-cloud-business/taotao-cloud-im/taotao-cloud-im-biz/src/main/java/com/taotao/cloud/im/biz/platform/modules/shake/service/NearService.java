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

package com.taotao.cloud.im.biz.platform.modules.shake.service;

import com.platform.modules.shake.vo.NearVo01;
import com.platform.modules.shake.vo.NearVo02;
import java.util.List;

/** 附近的人 服务层 */
public interface NearService {

    /** 发送经纬度 */
    List<NearVo02> doNear(NearVo01 nearVo);

    /** 关闭附近的人 */
    void closeNear();
}
