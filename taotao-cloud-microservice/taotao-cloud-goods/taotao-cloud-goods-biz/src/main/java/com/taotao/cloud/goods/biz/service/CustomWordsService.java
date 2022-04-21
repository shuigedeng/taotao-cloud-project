package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.vo.CustomWordsVO;
import com.taotao.cloud.goods.biz.entity.CustomWords;

/**
 * 自定义分词业务层
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
public interface CustomWordsService extends IService<CustomWords> {

	/**
	 * 自定义分词部署替换
	 *
	 * @return 替换的内容
	 */
	String deploy();

	/**
	 * 是否存在分词
	 *
	 * @param words 分词
	 * @return 是否存在
	 */
	Boolean existWords(String words);

	/**
	 * 添加自定义分词
	 *
	 * @param customWordsVO 自定义分词信息
	 * @return 是否添加成功
	 */
	Boolean addCustomWords(CustomWordsVO customWordsVO);

	/**
	 * 修改自定义分词
	 *
	 * @param customWordsVO 自定义分词信息
	 * @return 是否修改成功
	 */
	Boolean updateCustomWords(CustomWordsVO customWordsVO);

	/**
	 * 删除自定义分词
	 *
	 * @param id 自定义分词id
	 * @return 是否删除成功
	 */
	Boolean deleteCustomWords(Long id);

	/**
	 * 分页查询自定义分词
	 *
	 * @param words     分词
	 * @param pageParam 分页信息
	 * @return 自定义分词分页信息
	 */
	IPage<CustomWords> getCustomWordsByPage(String words, PageParam pageParam);

}
