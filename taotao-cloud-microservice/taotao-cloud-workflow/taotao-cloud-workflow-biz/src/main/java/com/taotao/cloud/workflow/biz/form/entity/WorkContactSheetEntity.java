package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 工作联系单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_workcontactsheet")
public class WorkContactSheetEntity {
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
     * 发件人
     */
    @TableField("F_DRAWPEOPLE")
    private String drawPeople;

    /**
     * 发件部门
     */
    @TableField("F_ISSUINGDEPARTMENT")
    private String issuingDepartment;

    /**
     * 发件日期
     */
    @TableField("F_TODATE")
    private Date toDate;

    /**
     * 收件部门
     */
    @TableField("F_SERVICEDEPARTMENT")
    private String serviceDepartment;

    /**
     * 收件人
     */
    @TableField("F_RECIPIENTS")
    private String recipients;

    /**
     * 收件日期
     */
    @TableField("F_COLLECTIONDATE")
    private Date collectionDate;

    /**
     * 协调事项
     */
    @TableField("F_COORDINATION")
    private String coordination;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;
}
