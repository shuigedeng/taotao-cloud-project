package com.taotao.cloud.office.easypoi.test.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 客户信息
 * @author JueYue
 *   2015-04-03 22:16:53
 * @version V1.0
 * 
 */
public class CustomerEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /** id */
    private String  id;
    /** 姓名 */
    @Excel(name = "姓名")
    private String  name;
    /** 性别 */
    @Excel(name = "性别")
    private String  sex;
    /** 资金 */
    @Excel(name = "资金")
    private String  money;
    /** 身份证号 */
    @Excel(name = "身份证号")
    private String  card;
    /** 住址 */
    @Excel(name = "住址")
    private String  address;
    /** 手机 */
    @Excel(name = "手机")
    private String  phone;
    /** QQ号 */
    @Excel(name = "QQ号")
    private String  qq;
    /** 开户时间 */
    @Excel(name = "开户时间")
    private java.util.Date    openTime;
    /** 交易账户 */
    @Excel(name = "交易账户")
    private String  tradeNum;
    /** 登录密码 */
    @Excel(name = "登录密码")
    private String  loginPassword;
    /** 交易密码 */
    @Excel(name = "交易密码")
    private String  tradePassword;
    /** 备注 */
    private String  memo;
    /** 客户状态 */
    @Excel(name = "客户状态")
    private String  status;
    /** 客户类型 */
    @Excel(name = "客户类型")
    private String  type;
    /** createTime */
    private java.util.Date    createTime;
    /** createUser */
    private String  createUser;
    /** updateTime */
    private java.util.Date    updateTime;
    /** updateUser */
    private String  updateUser;

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String id
     */

    public String getId() {
        return this.id;
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
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.Object
     * 
     * @return: java.lang.Object 性别
     */
    public String getSex() {
        return this.sex;
    }

    /**
     * 方法: 设置java.lang.Object
     * 
     * @param: java.lang.Object 性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 方法: 取得java.lang.Double
     * 
     * @return: java.lang.Double 资金
     */
    public String getMoney() {
        return this.money;
    }

    /**
     * 方法: 设置java.lang.Double
     * 
     * @param: java.lang.Double 资金
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 身份证号
     */
    public String getCard() {
        return this.card;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 身份证号
     */
    public void setCard(String card) {
        this.card = card;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 住址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 住址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 手机
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 手机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String QQ号
     */
    public String getQq() {
        return this.qq;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String QQ号
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 开户时间
     */
    public java.util.Date getOpenTime() {
        return this.openTime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 开户时间
     */
    public void setOpenTime(java.util.Date openTime) {
        this.openTime = openTime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 交易账户
     */
    public String getTradeNum() {
        return this.tradeNum;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 交易账户
     */
    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 登录密码
     */
    public String getLoginPassword() {
        return this.loginPassword;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 登录密码
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 交易密码
     */
    public String getTradePassword() {
        return this.tradePassword;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 交易密码
     */
    public void setTradePassword(String tradePassword) {
        this.tradePassword = tradePassword;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    public String getMemo() {
        return this.memo;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 客户状态
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 客户状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 客户类型
     */
    public String getType() {
        return this.type;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 客户类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date createTime
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date createTime
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String createUser
     */
    public String getCreateUser() {
        return this.createUser;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date updateTime
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date updateTime
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String updateUser
     */
    public String getUpdateUser() {
        return this.updateUser;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

}
