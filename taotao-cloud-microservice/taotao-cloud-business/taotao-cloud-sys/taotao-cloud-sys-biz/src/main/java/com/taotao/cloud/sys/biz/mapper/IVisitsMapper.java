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

package com.taotao.cloud.sys.biz.mapper;

import com.taotao.cloud.sys.biz.model.entity.system.Visits;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * IVisitsMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-29 08:57:49
 */
public interface IVisitsMapper extends MpSuperMapper<Visits, Long> {

    @Select("select * FROM visits where create_time between #{time1} and #{time2}")
    List<Visits> findAllVisits(@Param("time1") String time1, @Param("time2") String time2);
}
