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

package com.taotao.cloud.workflow.biz.flowable.bpm.dal.mysql.oa;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请假申请 Mapper
 *
 * @author jason
 * @author 芋道源码
 */
@Mapper
public interface BpmOALeaveMapper extends BaseMapperX<BpmOALeaveDO> {

    default PageResult<BpmOALeaveDO> selectPage(Long userId, BpmOALeavePageReqVO reqVO) {
        return selectPage(
                reqVO,
                new LambdaQueryWrapperX<BpmOALeaveDO>()
                        .eqIfPresent(BpmOALeaveDO::getUserId, userId)
                        .eqIfPresent(BpmOALeaveDO::getResult, reqVO.getResult())
                        .eqIfPresent(BpmOALeaveDO::getType, reqVO.getType())
                        .likeIfPresent(BpmOALeaveDO::getReason, reqVO.getReason())
                        .betweenIfPresent(BpmOALeaveDO::getCreateTime, reqVO.getCreateTime())
                        .orderByDesc(BpmOALeaveDO::getId));
    }
}
