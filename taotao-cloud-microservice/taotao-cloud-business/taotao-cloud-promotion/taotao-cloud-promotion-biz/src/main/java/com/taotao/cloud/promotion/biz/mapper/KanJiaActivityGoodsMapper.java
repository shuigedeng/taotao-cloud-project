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

package com.taotao.cloud.promotion.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.biz.model.bo.KanjiaActivityGoodsBO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 砍价活动商品数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:36:49
 */
public interface KanJiaActivityGoodsMapper extends MpSuperMapper<KanjiaActivityGoods, Long> {

    /**
     * 获取砍价商品VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 砍价商品VO分页
     */
    @Select("SELECT * FROM tt_kanjia_activity_goods ${ew.customSqlSegment}")
    IPage<KanjiaActivityGoodsBO> kanjiaActivityGoodsPage(
            IPage<KanjiaActivityGoods> page, @Param(Constants.WRAPPER) Wrapper<KanjiaActivityGoods> queryWrapper);
}
