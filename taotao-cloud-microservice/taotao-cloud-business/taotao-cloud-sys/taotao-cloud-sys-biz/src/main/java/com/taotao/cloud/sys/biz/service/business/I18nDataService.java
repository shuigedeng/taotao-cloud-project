package com.taotao.cloud.sys.biz.service.business;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.sys.biz.model.dto.I18nDataDTO;
import com.taotao.cloud.sys.biz.model.query.I18nDataQO;
import com.taotao.cloud.sys.biz.model.vo.I18nDataPageVO;
import com.taotao.cloud.sys.biz.model.entity.i18n.I18nData;

import java.util.List;

/**
 * 国际化信息
 */
public interface I18nDataService extends IService<I18nData> {

	/**
	 * 根据QueryObeject查询分页数据
	 *
	 * @param pageParam 分页参数
	 * @param qo        查询参数对象
	 * @return PageResult&lt;I18nDataPageVO&gt; 分页数据
	 */
	IPage<I18nDataPageVO> queryPage(PageQuery pageParam, I18nDataQO qo);

	/**
	 * 查询 i18nData 数据
	 *
	 * @param i18nDataQO 查询条件
	 * @return list
	 */
	List<I18nData> queryList(I18nDataQO i18nDataQO);

	/**
	 * 根据国际化标识查询 i18nData 数据
	 *
	 * @param code 国际化标识
	 * @return list
	 */
	List<I18nData> listByCode(String code);

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 *
	 * @param code        国际化标识
	 * @param languageTag 语言标签
	 * @return I18nData
	 */
	I18nData getByCodeAndLanguageTag(String code, String languageTag);

	/**
	 * 根据 code 和 languageTag 修改指定的 I18nData
	 *
	 * @param i18nDataDTO i18nDataDTO
	 * @return updated true or false
	 */
	boolean updateByCodeAndLanguageTag(I18nDataDTO i18nDataDTO);

	/**
	 * 根据 code 和 languageTag 删除指定的 I18nData
	 *
	 * @param code        国际化标识
	 * @param languageTag 语言标签
	 * @return delete true or false
	 */
	boolean removeByCodeAndLanguageTag(String code, String languageTag);

	/**
	 * 保存时跳过已存在的数据
	 *
	 * @param list 待保存的 I18nDataList
	 * @return List<I18nData> 已存在的数据
	 */
	List<I18nData> saveWhenNotExist(List<I18nData> list);

	/**
	 * 保存时,若数据已存在，则进行更新
	 *
	 * @param list 待保存的 I18nDataList
	 */
	void saveOrUpdate(List<I18nData> list);

}
