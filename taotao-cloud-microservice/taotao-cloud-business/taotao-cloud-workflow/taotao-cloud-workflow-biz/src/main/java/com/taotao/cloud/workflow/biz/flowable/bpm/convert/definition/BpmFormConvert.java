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

package com.taotao.cloud.workflow.biz.flowable.bpm.convert.definition;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormRespVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.definition.BpmFormDO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 动态表单 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmFormConvert {

    BpmFormConvert INSTANCE = Mappers.getMapper(BpmFormConvert.class);

    BpmFormDO convert(BpmFormCreateReqVO bean);

    BpmFormDO convert(BpmFormUpdateReqVO bean);

    BpmFormRespVO convert(BpmFormDO bean);

    List<BpmFormSimpleRespVO> convertList2(List<BpmFormDO> list);

    PageResult<BpmFormRespVO> convertPage(PageResult<BpmFormDO> page);
}
