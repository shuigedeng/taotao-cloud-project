package com.taotao.cloud.store.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.store.api.web.vo.StoreVO;
import com.taotao.cloud.store.biz.model.entity.Store;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 店铺数据处理层
 */
public interface StoreMapper extends BaseMapper<Store> {

	/**
	 * 获取店铺详细
	 *
	 * @param id 店铺ID
	 * @return 店铺VO
	 */
	@Select("select s.*,d.* from tt_store s inner join tt_store_detail d on s.id=d.store_id where s.id=#{id} ")
	StoreVO getStoreDetail(Long id);

	/**
	 * 获取店铺分页列表
	 *
	 * @param page         分页
	 * @param queryWrapper 查询条件
	 * @return 店铺VO分页列表
	 */
	@Select("select s.* from tt_store as s ${ew.customSqlSegment}")
	IPage<StoreVO> getStoreList(IPage<StoreVO> page,
								@Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);


	/**
	 * 修改店铺收藏数据
	 *
	 * @param storeId 店铺id
	 * @param num     收藏数量
	 */
	@Update("update tt_store set collection_num = collection_num + #{num} where id = #{storeId}")
	void updateCollection(String storeId, Integer num);

}
