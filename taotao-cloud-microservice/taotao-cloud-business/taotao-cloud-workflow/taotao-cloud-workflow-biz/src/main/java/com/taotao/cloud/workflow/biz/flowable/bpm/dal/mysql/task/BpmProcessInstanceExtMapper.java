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

package com.taotao.cloud.workflow.biz.flowable.bpm.dal.mysql.task;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.task.vo.instance.BpmProcessInstanceMyPageReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpmProcessInstanceExtMapper extends BaseMapperX<BpmProcessInstanceExtDO> {

    default PageResult<BpmProcessInstanceExtDO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO) {
        return selectPage(
                reqVO,
                new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                        .eqIfPresent(BpmProcessInstanceExtDO::getStartUserId, userId)
                        .likeIfPresent(BpmProcessInstanceExtDO::getName, reqVO.getName())
                        .eqIfPresent(BpmProcessInstanceExtDO::getProcessDefinitionId, reqVO.getProcessDefinitionId())
                        .eqIfPresent(BpmProcessInstanceExtDO::getCategory, reqVO.getCategory())
                        .eqIfPresent(BpmProcessInstanceExtDO::getStatus, reqVO.getStatus())
                        .eqIfPresent(BpmProcessInstanceExtDO::getResult, reqVO.getResult())
                        .betweenIfPresent(BpmProcessInstanceExtDO::getCreateTime, reqVO.getCreateTime())
                        .orderByDesc(BpmProcessInstanceExtDO::getId));
    }

    default BpmProcessInstanceExtDO selectByProcessInstanceId(String processInstanceId) {
        return selectOne(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceId);
    }

    default void updateByProcessInstanceId(BpmProcessInstanceExtDO updateObj) {
        update(
                updateObj,
                new LambdaQueryWrapperX<BpmProcessInstanceExtDO>()
                        .eq(BpmProcessInstanceExtDO::getProcessInstanceId, updateObj.getProcessInstanceId()));
    }
}
