package com.taotao.cloud.realtime.warehouse.datageneration.userlog_code.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.Error;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLog {
    private Common common;
    private Start start;
    private Page page;
    private String actions;
    private String displays;
    private Error err;
    private Long ts;
} 
