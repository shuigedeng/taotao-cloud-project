package com.taotao.cloud.quartz.one.core.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.starter.quartz.core.convert.QuartzJobConvert;
import cn.bootx.starter.quartz.dto.QuartzJobDto;
import cn.bootx.starter.quartz.param.QuartzJobParam;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("starter_quartz_job")
public class QuartzJob extends MpBaseEntity implements EntityBaseFunction<QuartzJobDto> {

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

    @Override
    public QuartzJobDto toDto() {
        return QuartzJobConvert.CONVERT.convert(this);
    }

    public static QuartzJob init(QuartzJobParam in){
        return QuartzJobConvert.CONVERT.convert(in);
    }
}
