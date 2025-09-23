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

package com.taotao.cloud.store.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import com.taotao.cloud.store.biz.model.entity.Store;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 店铺数据处理层
 */
public interface StoreMapper extends MpSuperMapper<Store, String> {

	/**
	 * 获取店铺详细
	 *
	 * @param id 店铺ID
	 * @return 店铺VO
	 */
	@Select("select s.*,d.* from tt_store s inner join tt_store_detail d on s.id=d.store_id where" + " s.id=#{id} ")
	StoreVO getStoreDetail(Long id);

	/**
	 * 获取店铺分页列表
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 店铺VO分页列表
	 */
	@Select("select s.* from tt_store as s ${ew.customSqlSegment}")
	IPage<StoreVO> getStoreList(IPage<StoreVO> page, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);

	/**
	 * 修改店铺收藏数据
	 *
	 * @param storeId 店铺id
	 * @param num     收藏数量
	 */
	@Update("update tt_store set collection_num = collection_num + #{num} where id = #{storeId}")
	void updateCollection(String storeId, Integer num);
}
