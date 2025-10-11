package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.data.mybatis.mybatisplus.query.LambdaQueryWrapperX;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.cloud.sys.biz.model.dto.I18nDataDTO;
import com.taotao.cloud.sys.biz.model.query.I18nDataQO;
import com.taotao.cloud.sys.biz.model.vo.I18nDataPageVO;
import com.taotao.cloud.sys.biz.model.convert.I18nDataConverter;
import com.taotao.cloud.sys.biz.model.entity.i18n.I18nData;

import java.util.List;

/**
 * 国际化信息
 */
public interface I18nDataMapper extends BaseMapper<I18nData> {

	/**
	 * 分页查询
	 *
	 * @param pageParam 分页参数
	 * @param qo        查询参数
	 * @return PageResult<I18nDataPageVO> VO分页数据
	 */
	default IPage<I18nDataPageVO> queryPage(PageQuery pageParam, I18nDataQO qo) {
		IPage<I18nData> page = MpUtils.buildMpPage(pageParam);
		Wrapper<I18nData> wrapper = buildQueryWrapper(qo);
		this.selectPage(page, wrapper);
		return page.convert(I18nDataConverter.INSTANCE::poToPageVo);
	}

	/**
	 * 根据 qo 构造查询 wrapper
	 *
	 * @param qo 查询条件
	 * @return LambdaQueryWrapperX
	 */
	default Wrapper<I18nData> buildQueryWrapper(I18nDataQO qo) {
		LambdaQueryWrapperX<I18nData> wrapper = new LambdaQueryWrapperX<>();
		wrapper.likeIfPresent(I18nData::getCode, qo.getCode())
			.likeIfPresent(I18nData::getMessage, qo.getMessage())
			.eqIfPresent(I18nData::getLanguageTag, qo.getLanguageTag());
		return wrapper;
	}

	/**
	 * 查询 i18nData 数据
	 *
	 * @param i18nDataQO 查询条件
	 * @return List
	 */
	default List<I18nData> queryList(I18nDataQO i18nDataQO) {
		Wrapper<I18nData> wrapper = buildQueryWrapper(i18nDataQO);
		return this.selectList(wrapper);
	}

	/**
	 * 查询 i18nData 数据
	 *
	 * @param code 国际化标识
	 * @return List
	 */
	default List<I18nData> listByCode(String code) {
		Wrapper<I18nData> wrapper = Wrappers.lambdaQuery(I18nData.class).eq(I18nData::getCode, code);
		return this.selectList(wrapper);
	}

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 *
	 * @param code        国际化标识
	 * @param languageTag 语言标签
	 * @return I18nData
	 */
	default I18nData selectByCodeAndLanguageTag(String code, String languageTag) {
		LambdaQueryWrapper<I18nData> wrapper = Wrappers.lambdaQuery(I18nData.class)
			.eq(I18nData::getCode, code)
			.eq(I18nData::getLanguageTag, languageTag);
		return this.selectOne(wrapper);
	}

	/**
	 * 根据 code 和 languageTag 修改指定的 I18nData
	 *
	 * @param i18nDataDTO i18nDataDTO
	 * @return updated true or false
	 */
	default boolean updateByCodeAndLanguageTag(I18nDataDTO i18nDataDTO) {
		LambdaUpdateWrapper<I18nData> wrapper = Wrappers.lambdaUpdate(I18nData.class)
			.eq(I18nData::getCode, i18nDataDTO.getCode())
			.eq(I18nData::getLanguageTag, i18nDataDTO.getLanguageTag());

		I18nData entity = new I18nData();
		entity.setMessage(i18nDataDTO.getMessage());
		entity.setRemarks(i18nDataDTO.getRemarks());

		return SqlHelper.retBool(this.update(entity, wrapper));
	}

	/**
	 * 根据 code 和 languageTag 删除指定的 I18nData
	 *
	 * @param code        国际化标识
	 * @param languageTag 语言标签
	 * @return I18nData
	 */
	default boolean deleteByCodeAndLanguageTag(String code, String languageTag) {
		LambdaQueryWrapper<I18nData> wrapper = Wrappers.lambdaQuery(I18nData.class)
			.eq(I18nData::getCode, code)
			.eq(I18nData::getLanguageTag, languageTag);
		return SqlHelper.retBool(this.delete(wrapper));
	}

	/**
	 * 查询已存在的 i18nData(根据 code 和 languageTag 联合唯一键)
	 *
	 * @param list i18nDataList
	 * @return List<I18nData>
	 */
	default List<I18nData> exists(List<I18nData> list) {
		// 组装 sql
		LambdaQueryWrapper<I18nData> wrapper = Wrappers.lambdaQuery(I18nData.class);
		for (I18nData i18nData : list) {
			wrapper.or(w -> {
				String code = i18nData.getCode();
				String languageTag = i18nData.getLanguageTag();
				w.eq(I18nData::getCode, code).eq(I18nData::getLanguageTag, languageTag);
			});
		}
		return this.selectList(wrapper);
	}

}
