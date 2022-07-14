package com.taotao.cloud.workflow.biz.engine.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流程可见
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("flow_enginevisible")
public class FlowEngineVisibleEntity {
    /**
     * 可见主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 流程主键
     */
    @TableField("F_FLOWID")
    private String flowId;

    /**
     * 经办类型
     */
    @TableField("F_OPERATORTYPE")
    private String operatorType;

    /**
     * 经办主键
     */
    @TableField("F_OPERATORID")
    private String operatorId;

    /**
     * 排序码
     */
    @TableField("F_SORTCODE")
    private Long sortCode;

    /**
     * 创建时间
     */
    @TableField(value = "F_CREATORTIME",fill = FieldFill.INSERT)
    private Date creatorTime;

    /**
     * 创建用户
     */
    @TableField(value = "F_CREATORUSERID",fill = FieldFill.INSERT)
    private String creatorUserId;
}
