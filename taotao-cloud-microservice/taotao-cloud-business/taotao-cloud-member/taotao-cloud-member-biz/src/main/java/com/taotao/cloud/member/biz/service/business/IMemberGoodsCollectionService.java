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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.member.sys.model.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.model.entity.MemberGoodsCollection;
import java.util.List;

/**
 * 商品收藏业务层
 *
 * @since 2020/11/18 2:25 下午
 */
public interface IMemberGoodsCollectionService extends IService<MemberGoodsCollection> {

    /**
     * 获取商品搜索分页
     *
     * @param PageQuery 查询参数
     * @return 商品搜索分页
     */
    IPage<GoodsCollectionVO> goodsCollection(PageQuery pageQuery);

    /**
     * 是否收藏商品
     *
     * @param skuId 规格ID
     * @return 是否收藏
     */
    Boolean isCollection(Long skuId);

    /**
     * 添加商品收藏
     *
     * @param skuId 规格ID
     * @return 操作状态
     */
    Boolean addGoodsCollection(Long skuId);

    /**
     * 商品收藏
     *
     * @param skuId 规格ID
     * @return 操作状态
     */
    Boolean deleteGoodsCollection(Long skuId);

    /**
     * 删除商品收藏
     *
     * @param goodsIds 规格ID
     * @return 操作状态
     */
    Boolean deleteGoodsCollection(List<Long> goodsIds);

    /**
     * 删除商品SKU收藏
     *
     * @param skuIds 规格ID
     * @return 操作状态
     */
    Boolean deleteSkuCollection(List<Long> skuIds);
}
