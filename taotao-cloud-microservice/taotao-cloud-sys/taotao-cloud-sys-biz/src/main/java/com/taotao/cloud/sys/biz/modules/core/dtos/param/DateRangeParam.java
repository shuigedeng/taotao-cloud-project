package com.taotao.cloud.sys.biz.modules.core.dtos.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 测试日期数据输入
 */
@Data
public class DateRangeParam {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;
}
