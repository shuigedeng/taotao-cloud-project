package com.taotao.cloud.operation.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.api.dto.PageDataDTO;
import com.taotao.cloud.operation.api.vo.PageDataListVO;
import com.taotao.cloud.operation.api.vo.PageDataVO;
import com.taotao.cloud.operation.biz.entity.PageData;

/**
 * 页面业务层
 */
public interface PageDataService extends IService<PageData> {

	/**
	 * 添加店铺页面 用于初次开店，生成店铺首页
	 *
	 * @param storeId 店铺ID
	 * @return 页面
	 */
	void addStorePageData(String storeId);

	/**
	 * 添加页面
	 *
	 * @param pageData 页面
	 * @return 页面
	 */
	PageData addPageData(PageData pageData);

	/**
	 * 修改页面
	 *
	 * @param pageData 页面
	 * @return 页面
	 */
	PageData updatePageData(PageData pageData);

	/**
	 * 发布页面
	 *
	 * @param id 页面ID
	 * @return 页面
	 */
	PageData releasePageData(String id);

	/**
	 * 删除页面
	 *
	 * @param id 页面ID
	 * @return 操作状态
	 */
	boolean removePageData(String id);

	/**
	 * 获取页面 用户前台页面展示
	 *
	 * @param pageDataDTO 页面数据DTO
	 * @return
	 */
	PageDataVO getPageData(PageDataDTO pageDataDTO);

	/**
	 * 页面分页
	 *
	 * @param pageVO      分页
	 * @param pageDataDTO 查询数据
	 * @return
	 */
	IPage<PageDataListVO> getPageDataList(PageVO pageVO, PageDataDTO pageDataDTO);
}
