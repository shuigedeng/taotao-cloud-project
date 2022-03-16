package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 邮箱设置
 *
 */
@Data
public class EmailSetting implements Serializable {

    private static final long serialVersionUID = 7261037221941716140L;
    @Schema(description =  "邮箱服务器")
    private String host;

    @Schema(description =  "发送者邮箱账号")
    private String username;

    @Schema(description =  "邮箱授权码")
    private String password;
}
