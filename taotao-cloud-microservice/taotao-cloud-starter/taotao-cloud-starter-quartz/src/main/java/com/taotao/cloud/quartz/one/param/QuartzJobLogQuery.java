package com.taotao.cloud.quartz.one.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* @author xxm
* @date 2022/5/2
*/
@Data
@Accessors(chain = true)
@Schema(title = "定时任务日志查询")
public class QuartzJobLogQuery {
    @Schema(description = "处理器全限定名")
    private String className;

    @Schema(description = "是否执行成功")
    private Boolean success;
}
