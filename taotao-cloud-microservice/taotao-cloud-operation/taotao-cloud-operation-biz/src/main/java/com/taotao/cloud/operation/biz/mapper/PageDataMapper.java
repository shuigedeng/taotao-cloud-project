package com.taotao.cloud.operation.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.operation.api.web.vo.PageDataListVO;
import com.taotao.cloud.operation.api.web.vo.PageDataVO;
import com.taotao.cloud.operation.biz.model.entity.PageData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 楼层装修设置数据处理层
 */
public interface PageDataMapper extends BaseSuperMapper<PageData> {

	/**
	 * 获取页面数据VO
	 *
	 * @param queryWrapper 查询条件
	 * @return 页面数据VO
	 */
	@Select("SELECT page_data FROM tt_page_data ${ew.customSqlSegment}")
	PageDataVO getPageData(@Param(Constants.WRAPPER) Wrapper<PageDataVO> queryWrapper);

	/**
	 * 获取页面数量
	 *
	 * @param queryWrapper 查询条件
	 * @return 页面数量
	 */
	@Select("SELECT COUNT(id) FROM tt_page_data ${ew.customSqlSegment}")
	Integer getPageDataNum(@Param(Constants.WRAPPER) Wrapper<Integer> queryWrapper);

	/**
	 * 获取页面数据分页
	 *
	 * @param page         页面
	 * @param queryWrapper 查询条件
	 * @return 页面数据分页
	 */
	@Select("SELECT id,name,page_show FROM tt_page_data ${ew.customSqlSegment}")
	IPage<PageDataListVO> getPageDataList(IPage<PageDataListVO> page, @Param(Constants.WRAPPER) Wrapper<PageDataListVO> queryWrapper);

}
