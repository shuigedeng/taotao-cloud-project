package com.taotao.cloud.quartz.one.dto;

import cn.bootx.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**   
* 定时任务
* @author xxm  
* @date 2021/11/2 
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "定时任务")
public class QuartzJobDto extends BaseDto {

    /** 任务名称 */
    private String name;

    /** 任务类名 */
    private String jobClassName;

    /** cron表达式 */
    private String cron;

    /** 参数 */
    private String parameter;

    /**
     * 状态
     * @see cn.bootx.starter.quartz.code.QuartzJobCode
     */
    private Integer state;

    /** 备注 */
    private String remark;
}
