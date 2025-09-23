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

package com.taotao.cloud.member.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.member.biz.model.entity.MemberStoreCollection;
import com.taotao.cloud.store.api.model.vo.StoreCollectionVO;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员收藏数据处理层
 */
public interface IStoreCollectionMapper extends MpSuperMapper<MemberStoreCollection, Long> {

    /**
     * 会员店铺收藏分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     */
    @Select("""
            select s.id,
            s.store_name,
            s.store_logo,
            s.self_operated
            from tt_store s INNER JOIN tt_store_collection sc
            ON s.id=sc.store_id ${ew.customSqlSegment}
            """)
    IPage<StoreCollectionVO> storeCollectionVOList(
            IPage<StoreCollectionVO> page, @Param(Constants.WRAPPER) Wrapper<StoreCollectionVO> queryWrapper);
}
