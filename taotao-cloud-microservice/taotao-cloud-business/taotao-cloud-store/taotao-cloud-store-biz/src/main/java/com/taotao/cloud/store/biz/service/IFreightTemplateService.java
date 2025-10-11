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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.store.api.model.vo.FreightTemplateInfoVO;
import com.taotao.cloud.store.biz.model.entity.FreightTemplate;
import java.util.List;

/**
 * 店铺地址（自提点）详细业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 14:59:36
 */
public interface IFreightTemplateService extends IService<FreightTemplate> {

    /**
     * 获取当前商家的运费模板列表
     *
     * @param PageQuery 分页
     * @return {@link IPage }<{@link FreightTemplate }>
     * @since 2022-06-01 14:59:37
     */
    IPage<FreightTemplate> getFreightTemplate(PageQuery PageQuery);

    /**
     * 获取商家的运费模板
     *
     * @param storeId
     * @return {@link List }<{@link FreightTemplateInfoVO }>
     * @since 2022-06-01 14:59:37
     */
    List<FreightTemplateInfoVO> getFreightTemplateList(String storeId);

    /**
     * 获取运费模板详细信息
     *
     * @param id 运费模板ID
     * @return {@link FreightTemplateInfoVO }
     * @since 2022-06-01 14:59:37
     */
    FreightTemplateInfoVO getFreightTemplate(String id);

    /**
     * 添加商家运费模板 运费模板分为卖家包邮、运费计算两种类型
     *
     * @param freightTemplateInfoVO 运费模板
     * @return {@link FreightTemplateInfoVO }
     * @since 2022-06-01 14:59:37
     */
    FreightTemplateInfoVO addFreightTemplate(FreightTemplateInfoVO freightTemplateInfoVO);

    /**
     * 修改商家运费模板
     *
     * @param freightTemplateInfoVO 运费模板
     * @return {@link FreightTemplateInfoVO }
     * @since 2022-06-01 14:59:37
     */
    FreightTemplateInfoVO editFreightTemplate(FreightTemplateInfoVO freightTemplateInfoVO);

    /**
     * 删除商家运费模板 删除模板并删除模板的配置内容
     *
     * @param id 运费模板ID
     * @return boolean
     * @since 2022-06-01 14:59:37
     */
    boolean removeFreightTemplate(Long id);
}
