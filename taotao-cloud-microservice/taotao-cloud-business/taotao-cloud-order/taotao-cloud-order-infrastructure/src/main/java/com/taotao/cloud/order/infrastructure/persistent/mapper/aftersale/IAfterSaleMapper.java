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

package com.taotao.cloud.order.infrastructure.persistent.mapper.aftersale;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.order.infrastructure.persistent.po.aftersale.AfterSalePO;
import com.taotao.cloud.order.sys.model.vo.aftersale.AfterSaleVO;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 售后数据处理层
 *
 * @author shuigedeng
 */
public interface IAfterSaleMapper extends BaseSuperMapper<AfterSalePO, Long> {

    /**
     * 获取售后VO分页
     *
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 售后VO分页
     */
    @Select("SELECT * FROM tt_after_sale ${ew.customSqlSegment}")
    IPage<AfterSalePO> queryByParams(
            IPage<AfterSaleVO> page, @Param(Constants.WRAPPER) Wrapper<AfterSaleVO> queryWrapper);
}
