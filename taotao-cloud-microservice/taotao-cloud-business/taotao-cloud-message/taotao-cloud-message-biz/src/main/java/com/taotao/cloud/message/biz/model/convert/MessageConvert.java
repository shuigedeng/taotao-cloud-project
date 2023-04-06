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

package com.taotao.cloud.message.biz.model.convert;

import com.taotao.cloud.message.biz.model.entity.Message;
import com.taotao.cloud.sys.api.model.vo.dept.DeptTreeVO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * DeptMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:18
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageConvert {

    /** 实例 */
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    /**
     * 部门列表给签证官
     *
     * @param deptList 部门列表
     * @return {@link List }<{@link DeptTreeVO }>
     * @since 2022-04-28 13:39:18
     */
    List<DeptTreeVO> convertTree(List<Message> deptList);
}
