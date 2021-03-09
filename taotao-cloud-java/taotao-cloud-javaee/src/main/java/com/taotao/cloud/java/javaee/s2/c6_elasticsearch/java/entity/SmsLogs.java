package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsLogs {

    private String id;// 唯一ID 1

    private Date createDate;// 创建时间

    private Date sendDate; // 发送时间

    private String longCode;// 发送的长号码

    private String mobile;// 下发手机号

    private String corpName;// 发送公司名称

    private String smsContent; // 下发短信内容

    private Integer state; // 短信下发状态 0 成功 1 失败

    private Integer operatorId; // '运营商编号 1 移动 2 联通 3 电信

    private String province;// 省份

    private String ipAddr; //下发服务器IP地址

    private Integer replyTotal; //短信状态报告返回时长（秒）

    private Integer fee;  // 费用

}
