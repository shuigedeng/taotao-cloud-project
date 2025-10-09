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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.PageQuery;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.cloud.goods.biz.model.vo.CustomWordsVO;
import com.taotao.cloud.goods.biz.mapper.ICustomWordsMapper;
import com.taotao.cloud.goods.biz.model.convert.CustomWordsConvert;
import com.taotao.cloud.goods.biz.model.entity.CustomWords;
import com.taotao.cloud.goods.biz.repository.CustomWordsRepository;
import com.taotao.cloud.goods.biz.repository.ICustomWordsRepository;
import com.taotao.cloud.goods.biz.service.business.ICustomWordsService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
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
        extends BaseSuperServiceImpl<CustomWords, Long,ICustomWordsMapper, CustomWordsRepository, ICustomWordsRepository>
        implements ICustomWordsService {

    @Override
    public String deploy() {
        LambdaQueryWrapper<CustomWords> queryWrapper =
                new LambdaQueryWrapper<CustomWords>().eq(CustomWords::getDisabled, 1);
        List<CustomWords> list = list(queryWrapper);

        HttpServletResponse response = RequestUtils.getResponse();
        StringBuilder builder = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            boolean flag = true;
            for (CustomWords customWords : list) {
                if (flag) {
                    try {
                        response.setHeader(
                                "Last-Modified", customWords.getCreateTime().toString());
                        response.setHeader("ETag", Integer.toString(list.size()));
                    } catch (Exception e) {
                        LogUtils.error("自定义分词错误", e);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomWords(CustomWordsVO customWordsVO) {
        LambdaQueryWrapper<CustomWords> queryWrapper =
                new LambdaQueryWrapper<CustomWords>().eq(CustomWords::getName, customWordsVO.getName());
        CustomWords one = this.getOne(queryWrapper, false);
        if (one != null && one.getDisabled().equals(1)) {
            throw new BusinessException(ResultEnum.CUSTOM_WORDS_EXIST_ERROR);
        } else if (one != null && !one.getDisabled().equals(1)) {
            this.remove(queryWrapper);
        }
        customWordsVO.setDisabled(1);
        return this.save(CustomWordsConvert.INSTANCE.convert(customWordsVO));
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
    public boolean updateCustomWords(CustomWordsVO customWordsVO) {
        if (this.getById(customWordsVO.getId()) == null) {
            throw new BusinessException(ResultEnum.CUSTOM_WORDS_NOT_EXIST_ERROR);
        }

        return this.updateById(CustomWordsConvert.INSTANCE.convert(customWordsVO));
    }

    @Override
    public IPage<CustomWords> getCustomWordsByPage(String words, PageQuery pageQuery) {
        LambdaQueryWrapper<CustomWords> queryWrapper =
                new LambdaQueryWrapper<CustomWords>().like(CustomWords::getName, words);
        return this.page(pageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public boolean existWords(String words) {
        LambdaQueryWrapper<CustomWords> queryWrapper =
                new LambdaQueryWrapper<CustomWords>().eq(CustomWords::getName, words);
        long count = count(queryWrapper);
        return count > 0;
    }
}
