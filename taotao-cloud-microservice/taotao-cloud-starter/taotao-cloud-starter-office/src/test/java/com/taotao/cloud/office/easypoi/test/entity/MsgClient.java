package com.taotao.cloud.office.easypoi.test.entity;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

/**
 * @Title: Entity
 * @Description: 通讯录
 * @author JueYue
 *   2014-09-22 16:03:32
 * @version V1.0
 * 
 */
@SuppressWarnings("serial")
public class MsgClient implements java.io.Serializable {
    /** id */
    private String id;
    // 电话号码(主键)
    @Excel(name = "电话号码")
    private String           clientPhone = null;
    // 客户姓名
    @Excel(name = "姓名")
    private String           clientName  = null;
    // 所属分组
    @ExcelEntity
    private MsgClientGroup   group       = null;
    // 备注
    @Excel(name = "备注")
    private String           remark      = null;
    // 生日
    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20)
    private Date             birthday    = null;
    // 创建人
    private String           createBy    = null;

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 生日
     */
    public Date getBirthday() {
        return this.birthday;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 客户姓名
     */
    public String getClientName() {
        return this.clientName;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 电话号码
     */
    public String getClientPhone() {
        return this.clientPhone;
    }

    public String getCreateBy() {
        return createBy;
    }

    public MsgClientGroup getGroup() {
        return group;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String id
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 客户姓名
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 电话号码
     */
    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setGroup(MsgClientGroup group) {
        this.group = group;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param remark
     * 
     * @param: java.lang.String 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
