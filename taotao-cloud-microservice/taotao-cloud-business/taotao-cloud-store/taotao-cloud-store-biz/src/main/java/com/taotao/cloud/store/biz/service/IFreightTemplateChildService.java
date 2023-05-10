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

package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.biz.model.entity.FreightTemplateChild;
import java.util.List;

/**
 * 配送子模板业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 14:59:03
 */
public interface IFreightTemplateChildService extends IService<FreightTemplateChild> {

    /**
     * 获取当前商家的运费模板子内容列表
     *
     * @param freightTemplateId 运费模板ID
     * @return {@link List }<{@link FreightTemplateChild }>
     * @since 2022-06-01 14:59:03
     */
    List<FreightTemplateChild> getFreightTemplateChild(String freightTemplateId);

    /**
     * 添加商家运费模板
     *
     * @param freightTemplateChildren 子模板信息
     * @return boolean
     * @since 2022-06-01 14:59:03
     */
    boolean addFreightTemplateChild(List<FreightTemplateChild> freightTemplateChildren);

    /**
     * 删除商家运费模板
     *
     * @param freightTemplateId 运费模板ID
     * @return boolean
     * @since 2022-06-01 14:59:03
     */
    boolean removeFreightTemplate(Long freightTemplateId);
}
