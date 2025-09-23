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

package com.taotao.cloud.operation.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.operation.api.model.vo.PageDataListVO;
import com.taotao.cloud.operation.api.model.vo.PageDataVO;
import com.taotao.cloud.operation.biz.model.entity.PageData;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 楼层装修设置数据处理层 */
public interface PageDataMapper extends MpSuperMapper<PageData, Long> {

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
     * @param page 页面
     * @param queryWrapper 查询条件
     * @return 页面数据分页
     */
    @Select("SELECT id,name,page_show FROM tt_page_data ${ew.customSqlSegment}")
    IPage<PageDataListVO> getPageDataList(
            IPage<PageDataListVO> page, @Param(Constants.WRAPPER) Wrapper<PageDataListVO> queryWrapper);
}
