package com.taotao.cloud.quartz.one.core.convert;

import cn.bootx.starter.quartz.core.entity.QuartzJob;
import cn.bootx.starter.quartz.core.entity.QuartzJobLog;
import cn.bootx.starter.quartz.dto.QuartzJobDto;
import cn.bootx.starter.quartz.dto.QuartzJobLogDto;
import cn.bootx.starter.quartz.param.QuartzJobParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**   
* 定时任务转换
* @author xxm  
* @date 2021/11/2 
*/
@Mapper
public interface QuartzJobConvert {
    QuartzJobConvert CONVERT = Mappers.getMapper(QuartzJobConvert.class);

    QuartzJobDto convert(QuartzJob in);

    QuartzJob convert(QuartzJobParam in);

    QuartzJobLogDto convert(QuartzJobLog in);
}
