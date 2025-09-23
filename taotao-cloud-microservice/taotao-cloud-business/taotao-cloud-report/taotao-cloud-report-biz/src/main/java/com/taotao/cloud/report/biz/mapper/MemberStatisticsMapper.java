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

package com.taotao.cloud.report.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import com.taotao.cloud.report.biz.model.entity.MemberStatisticsData;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 会员统计数据处理层 */
public interface MemberStatisticsMapper  {

    /**
     * 获取会员统计数量
     *
     * @param queryWrapper 查询条件
     * @return 会员统计数量
     */
    @Select("SELECT  COUNT(0)  FROM tt_member  ${ew.customSqlSegment}")
    long customSqlQuery(@Param(Constants.WRAPPER) Wrapper queryWrapper);

    /**
     * 获取会员分布列表
     *
     * @return 会员分布列表
     */
//    @Select("select client_enum,count(0) as num from tt_member group by client_enum")
//    List<MemberDistributionVO> distribution();
}
