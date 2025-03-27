package com.taotao.cloud.iot.biz.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;

/**
 * tcp通讯数据包
 *
 * @author 
 */
@Data
@Schema(description = "tcp通讯数据包装类")
public class TcpMsgDTO {
    private String topic;
    private Object msg;
}
