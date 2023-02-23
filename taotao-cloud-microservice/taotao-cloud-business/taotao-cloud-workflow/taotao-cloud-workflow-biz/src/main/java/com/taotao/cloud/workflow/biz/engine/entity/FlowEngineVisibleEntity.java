package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.util.Date;
import lombok.Data;

/**
 * 流程可见
 *
 */
@Data
@TableName("flow_enginevisible")
public class FlowEngineVisibleEntity extends SuperEntity<FlowEngineVisibleEntity, String> {
    /**
     * 可见主键
     */
    @TableId("id")
    private String id;

    /**
     * 流程主键
     */
    @TableField("flow_id")
    private String flowId;

    /**
     * 经办类型
     */
    @TableField("operator_type")
    private String operatorType;

    /**
     * 经办主键
     */
    @TableField("operator_id")
    private String operatorId;

    /**
     * 排序码
     */
    @TableField("sort_code")
    private Long sortCode;

    /**
     * 创建时间
     */
    @TableField(value = "creator_time",fill = FieldFill.INSERT)
    private Date creatorTime;

    /**
     * 创建用户
     */
    @TableField(value = "creator_user_id",fill = FieldFill.INSERT)
    private String creatorUserId;
}
