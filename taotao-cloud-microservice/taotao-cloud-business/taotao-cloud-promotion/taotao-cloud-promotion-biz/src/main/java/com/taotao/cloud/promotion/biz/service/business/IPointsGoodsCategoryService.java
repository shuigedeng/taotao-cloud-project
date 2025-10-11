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

package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.biz.model.entity.PointsGoodsCategory;

/**
 * 积分商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:02
 */
public interface IPointsGoodsCategoryService extends IService<PointsGoodsCategory> {

    /**
     * 添加积分商品分类
     *
     * @param pointsGoodsCategory 积分商品分类信息
     * @return boolean
     * @since 2022-04-27 16:44:02
     */
    boolean addCategory(PointsGoodsCategory pointsGoodsCategory);

    /**
     * 更新积分商品分类
     *
     * @param pointsGoodsCategory 积分商品分类信息
     * @return boolean
     * @since 2022-04-27 16:44:02
     */
    boolean updateCategory(PointsGoodsCategory pointsGoodsCategory);

    /**
     * 删除积分商品类型
     *
     * @param id 积分商品分类id
     * @return boolean
     * @since 2022-04-27 16:44:02
     */
    boolean deleteCategory(String id);

    /**
     * 分页获取积分商品类型
     *
     * @param name 类型名称
     * @param page 分页参数
     * @return {@link IPage }<{@link PointsGoodsCategory }>
     * @since 2022-04-27 16:44:02
     */
    IPage<PointsGoodsCategory> getCategoryByPage(String name, PageQuery page);

    /**
     * 获取积分商品类型详情
     *
     * @param id 积分商品类型id
     * @return {@link PointsGoodsCategory }
     * @since 2022-04-27 16:44:02
     */
    PointsGoodsCategory getCategoryDetail(String id);
}
