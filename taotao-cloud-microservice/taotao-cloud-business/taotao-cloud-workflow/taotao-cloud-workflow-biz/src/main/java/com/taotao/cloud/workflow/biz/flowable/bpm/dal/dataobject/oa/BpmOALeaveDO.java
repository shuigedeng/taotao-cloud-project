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

package com.taotao.cloud.workflow.biz.flowable.bpm.dal.dataobject.oa;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * OA 请假申请 DO
 *
 * <p>{@link #day} 请假天数，目前先简单做。一般是分成请假上午和下午，可以是 1 整天，可以是 0.5 半天
 *
 * @author jason
 * @author 芋道源码
 */
@TableName("bpm_oa_leave")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmOALeaveDO extends BaseDO {

    /** 请假表单主键 */
    @TableId
    private Long id;
    /**
     * 申请人的用户编号
     *
     * <p>关联 AdminUserDO 的 id 属性
     */
    private Long userId;
    /** 请假类型 */
    @TableField("`type`")
    private String type;
    /** 原因 */
    private String reason;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 请假天数 */
    private Long day;
    /**
     * 请假的结果
     *
     * <p>枚举 {@link BpmProcessInstanceResultEnum} 考虑到简单，所以直接复用了 BpmProcessInstanceResultEnum
     * 枚举，也可以自己定义一个枚举哈
     */
    private Integer result;

    /**
     * 对应的流程编号
     *
     * <p>关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
}
