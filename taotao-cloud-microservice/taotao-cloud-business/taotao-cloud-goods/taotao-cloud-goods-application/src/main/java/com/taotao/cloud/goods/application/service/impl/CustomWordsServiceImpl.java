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

package com.taotao.cloud.goods.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.cloud.goods.application.command.goods.dto.clientobject.CustomWordsCO;
import com.taotao.cloud.goods.application.convert.CustomWordsConvert;
import com.taotao.cloud.goods.application.service.ICustomWordsService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICustomWordsMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CustomWordsPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.CustomWordsRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.ICustomWordsRepository;
import com.taotao.boot.web.base.service.impl.BaseSuperServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义分词业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:21
 */
@Service
public class CustomWordsServiceImpl
	extends
	BaseSuperServiceImpl<CustomWordsPO, Long, ICustomWordsMapper, CustomWordsRepository, ICustomWordsRepository>
	implements ICustomWordsService {

	@Override
	public String deploy() {
		LambdaQueryWrapper<CustomWordsPO> queryWrapper =
			new LambdaQueryWrapper<CustomWordsPO>().eq(CustomWordsPO::getDisabled, 1);
		List<CustomWordsPO> list = list(queryWrapper);

		HttpServletResponse response = RequestUtils.getResponse();
		StringBuilder builder = new StringBuilder();
		if (list != null && !list.isEmpty()) {
			boolean flag = true;
			for (CustomWordsPO customWordsPO : list) {
				if (flag) {
					try {
						response.setHeader(
							"Last-Modified", customWordsPO.getCreateTime().toString());
						response.setHeader("ETag", Integer.toString(list.size()));
					}
					catch (Exception e) {
						LogUtils.error("自定义分词错误", e);
					}
					builder.append(customWordsPO.getName());
					flag = false;
				}
				else {
					builder.append("\n");
					builder.append(customWordsPO.getName());
				}
			}
		}

		return new String(builder.toString().getBytes(StandardCharsets.UTF_8));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addCustomWords(CustomWordsCO customWordsCO) {
		LambdaQueryWrapper<CustomWordsPO> queryWrapper =
			new LambdaQueryWrapper<CustomWordsPO>().eq(CustomWordsPO::getName,
				customWordsCO.getName());
		CustomWordsPO one = this.getOne(queryWrapper, false);
		if (one != null && one.getDisabled().equals(1)) {
			throw new BusinessException(ResultEnum.CUSTOM_WORDS_EXIST_ERROR);
		}
		else if (one != null && !one.getDisabled().equals(1)) {
			this.remove(queryWrapper);
		}
		customWordsCO.setDisabled(1);
		return this.save(CustomWordsConvert.INSTANCE.convert(customWordsCO));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteCustomWords(Long id) {
		if (this.getById(id) == null) {
			throw new BusinessException(ResultEnum.CUSTOM_WORDS_NOT_EXIST_ERROR);
		}
		return this.removeById(id);
	}

	@Override
	public boolean updateCustomWords(CustomWordsCO customWordsCO) {
		if (this.getById(customWordsCO.getId()) == null) {
			throw new BusinessException(ResultEnum.CUSTOM_WORDS_NOT_EXIST_ERROR);
		}

		return this.updateById(CustomWordsConvert.INSTANCE.convert(customWordsCO));
	}

	@Override
	public IPage<CustomWordsPO> getCustomWordsByPage(String words, PageQuery pageQuery) {
		LambdaQueryWrapper<CustomWordsPO> queryWrapper =
			new LambdaQueryWrapper<CustomWordsPO>().like(CustomWordsPO::getName, words);
		return this.page(pageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	public boolean existWords(String words) {
		LambdaQueryWrapper<CustomWordsPO> queryWrapper =
			new LambdaQueryWrapper<CustomWordsPO>().eq(CustomWordsPO::getName, words);
		long count = count(queryWrapper);
		return count > 0;
	}
}
