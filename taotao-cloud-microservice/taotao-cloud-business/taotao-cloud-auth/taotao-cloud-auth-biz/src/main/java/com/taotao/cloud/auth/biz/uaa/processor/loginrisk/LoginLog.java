package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class LoginLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    private Integer result;

    private String cityCode;

    private String ip;

    private Date time;
}
