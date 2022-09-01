package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.model.vo.CustomWordsVO;
import com.taotao.cloud.goods.biz.model.entity.CustomWords;

/**
 * 自定义分词业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:50
 */
public interface ICustomWordsService extends IService<CustomWords> {

	/**
	 * 自定义分词部署替换
	 *
	 * @return {@link String }
	 * @since 2022-04-27 16:59:50
	 */
	String deploy();

	/**
	 * 是否存在分词
	 *
	 * @param words 分词
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:50
	 */
	Boolean existWords(String words);

	/**
	 * 添加自定义分词
	 *
	 * @param customWordsVO 自定义分词信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:50
	 */
	Boolean addCustomWords(CustomWordsVO customWordsVO);

	/**
	 * 修改自定义分词
	 *
	 * @param customWordsVO 自定义分词信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:51
	 */
	Boolean updateCustomWords(CustomWordsVO customWordsVO);

	/**
	 * 删除自定义分词
	 *
	 * @param id 自定义分词id
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:51
	 */
	Boolean deleteCustomWords(Long id);

	/**
	 * 分页查询自定义分词
	 *
	 * @param words     分词
	 * @param pageParam 分页信息
	 * @return {@link IPage }<{@link CustomWords }>
	 * @since 2022-04-27 16:59:51
	 */
	IPage<CustomWords> getCustomWordsByPage(String words, PageParam pageParam);

}
