package com.taotao.cloud.quartz.one.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* 定时任务
* @author xxm
* @date 2021/11/2
*/
@Data
@Accessors(chain = true)
@Schema(title = "定时任务")
public class QuartzJobParam {

    @Schema(description = "主键")
    private Long id;

    /** 任务名称 */
    @Schema(description = "主键")
    private String name;

    @Schema(description = "任务类名")
    private String jobClassName;

    @Schema(description = "cron表达式")
    private String cron;

    @Schema(description = "参数")
    private String parameter;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "备注")
    private String remark;
}
