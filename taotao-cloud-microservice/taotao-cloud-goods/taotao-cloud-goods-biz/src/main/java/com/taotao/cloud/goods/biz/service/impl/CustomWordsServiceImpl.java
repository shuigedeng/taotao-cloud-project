package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.servlet.RequestUtil;
import com.taotao.cloud.goods.api.vo.CustomWordsVO;
import com.taotao.cloud.goods.biz.entity.CustomWords;
import com.taotao.cloud.goods.biz.mapper.CustomWordsMapper;
import com.taotao.cloud.goods.biz.service.CustomWordsService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义分词业务层实现
 **/
@Service
public class CustomWordsServiceImpl extends ServiceImpl<CustomWordsMapper, CustomWords> implements
	CustomWordsService {

	@Override
	public String deploy() {
		LambdaQueryWrapper<CustomWords> queryWrapper = new LambdaQueryWrapper<CustomWords>().eq(
			CustomWords::getDisabled, 1);
		List<CustomWords> list = list(queryWrapper);

		HttpServletResponse response = RequestUtil.getResponse();
		StringBuilder builder = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			boolean flag = true;
			for (CustomWords customWords : list) {
				if (flag) {
					try {
						response.setHeader("Last-Modified", customWords.getCreateTime().toString());
						response.setHeader("ETag", Integer.toString(list.size()));
					} catch (Exception e) {
						log.error("自定义分词错误", e);
					}
					builder.append(customWords.getName());
					flag = false;
				} else {
					builder.append("\n");
					builder.append(customWords.getName());
				}
			}
		}

		return new String(builder.toString().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 添加自定义分词
	 *
	 * @param customWordsVO 自定义分词信息
	 * @return 是否添加成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addCustomWords(CustomWordsVO customWordsVO) {
		//LambdaQueryWrapper<CustomWords> queryWrapper = new LambdaQueryWrapper<CustomWords>().eq(
		//	CustomWords::getName, customWordsVO.getName());
		//CustomWords one = this.getOne(queryWrapper, false);
		//if (one != null && one.getDisabled().equals(1)) {
		//	throw new BusinessException(ResultEnum.CUSTOM_WORDS_EXIST_ERROR);
		//} else if (one != null && !one.getDisabled().equals(1)) {
		//	this.remove(queryWrapper);
		//}
		//customWordsVO.setDisabled(1);
		//return this.save(customWordsVO);
		return true;
	}

	/**
	 * 删除自定义分词
	 *
	 * @param id 自定义分词id
	 * @return 是否删除成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteCustomWords(String id) {
		if (this.getById(id) == null) {
			throw new BusinessException(ResultEnum.CUSTOM_WORDS_NOT_EXIST_ERROR);
		}
		return this.removeById(id);
	}

	@Override
	public Boolean updateCustomWords(CustomWordsVO customWordsVO) {
		//if (this.getById(customWordsVO.getId()) == null) {
		//	throw new BusinessException(ResultEnum.CUSTOM_WORDS_NOT_EXIST_ERROR);
		//}
		//
		//return this.updateById(customWordsVO);
		return true;
	}

	@Override
	public IPage<CustomWords> getCustomWordsByPage(String words,  PageParam pageParam) {
		LambdaQueryWrapper<CustomWords> queryWrapper = new LambdaQueryWrapper<CustomWords>().like(
			CustomWords::getName, words);
		return this.page(pageParam.buildMpPage(), queryWrapper);
	}

	@Override
	public Boolean existWords(String words) {
		LambdaQueryWrapper<CustomWords> queryWrapper = new LambdaQueryWrapper<CustomWords>().eq(
			CustomWords::getName, words);
		long count = count(queryWrapper);
		return count > 0;
	}
}
