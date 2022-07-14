package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 月工作总结
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("wform_monthlyreport")
public class MonthlyReportEntity {
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
     * 创建人
     */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /**
     * 创建日期
     */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /**
     * 所属部门
     */
    @TableField("F_APPLYDEPT")
    private String applyDept;

    /**
     * 所属职务
     */
    @TableField("F_APPLYPOST")
    private String applyPost;

    /**
     * 完成时间
     */
    @TableField("F_PLANENDTIME")
    private Date planEndTime;

    /**
     * 总体评价
     */
    @TableField("F_OVERALEVALUAT")
    private String overalEvaluat;

    /**
     * 工作事项
     */
    @TableField("F_NPWORKMATTER")
    private String nPWorkMatter;

    /**
     * 次月日期
     */
    @TableField("F_NPFINISHTIME")
    private Date nPFinishTime;

    /**
     * 次月目标
     */
    @TableField("F_NFINISHMETHOD")
    private String nFinishMethod;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;
}
