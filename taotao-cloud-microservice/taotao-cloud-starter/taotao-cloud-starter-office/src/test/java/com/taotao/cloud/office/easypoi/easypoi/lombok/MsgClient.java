package com.taotao.cloud.office.easypoi.easypoi.lombok;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

import java.util.Date;


/**
 * @Title: Entity
 * @Description: 通讯录
 * @author JueYue
 *   2014-09-22 16:03:32
 * @version V1.0
 * 
 */
public class MsgClient implements java.io.Serializable {
    // 电话号码(主键)
    @Excel(name = "电话号码")
    private String           clientPhone = null;
    // 客户姓名
    @Excel(name = "姓名")
    private String           clientName  = null;
    // 备注
    @Excel(name = "备注")
    private String           remark      = null;
    // 生日
    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20)
    private Date             birthday    = null;

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
