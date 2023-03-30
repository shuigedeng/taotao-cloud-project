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

package com.taotao.cloud.operation.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.biz.model.entity.Special;

/**
 * 专题活动业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 15:06:11
 */
public interface SpecialService extends IService<Special> {

    /**
     * 添加专题活动
     *
     * @param special 专题活动
     * @return {@link Special }
     * @since 2022-06-02 15:06:11
     */
    Special addSpecial(Special special);

    /**
     * 删除专题活动
     *
     * @param id 活动ID
     * @return boolean
     * @since 2022-06-02 15:06:11
     */
    boolean removeSpecial(String id);
}
