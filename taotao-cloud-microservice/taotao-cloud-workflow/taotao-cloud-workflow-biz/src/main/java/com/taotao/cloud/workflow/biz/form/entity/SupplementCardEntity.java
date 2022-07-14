package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 补卡申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_supplementcard")
public class SupplementCardEntity {
    /**
     * 主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 流程主键
     */
    @TableField("F_FLOWID")
    private String flowId;

    /**
     * 流程标题
     */
    @TableField("F_FLOWTITLE")
    private String flowTitle;

    /**
     * 紧急程度
     */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /**
     * 流程单据
     */
    @TableField("F_BILLNO")
    private String billNo;

    /**
     * 员工姓名
     */
    @TableField("F_FULLNAME")
    private String fullName;

    /**
     * 所在部门
     */
    @TableField("F_DEPARTMENT")
    private String department;

    /**
     * 所在职务
     */
    @TableField("F_POSITION")
    private String position;

    /**
     * 申请日期
     */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /**
     * 证明人
     */
    @TableField("F_WITNESS")
    private String witness;

    /**
     * 补卡次数
     */
    @TableField("F_SUPPLEMENTNUM")
    private String supplementNum;

    /**
     * 开始时间
     */
    @TableField("F_STARTTIME")
    private Date startTime;

    /**
     * 备注
     */
    @TableField("F_DESCRIPTION")
    private String description;

    /**
     * 结束日期
     */
    @TableField("F_ENDTIME")
    private Date endTime;
}
