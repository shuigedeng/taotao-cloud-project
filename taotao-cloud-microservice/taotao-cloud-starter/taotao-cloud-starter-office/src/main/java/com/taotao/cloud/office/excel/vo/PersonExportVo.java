package com.taotao.cloud.office.excel.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
//@Data
//@EqualsAndHashCode(callSuper = false)
//@AllArgsConstructor
//@NoArgsConstructor
public class PersonExportVo implements Serializable {

    private static final long serialVersionUID = 279232352518402126L;
    /**
     * 姓名
     */
    @Excel(name = "姓名", orderNum = "0", width = 15)
    private String name;

    /**
     * 登录用户名
     */
    @Excel(name = "用户名", orderNum = "1", width = 15)
    private String username;

    @Excel(name = "手机号码", orderNum = "2", width = 15)
    private String phoneNumber;

    /**
     * 人脸图片
     */
    @Excel(name = "人脸图片", orderNum = "3", width = 15, type = 2)
    private String imageUrl;
}
