package com.taotao.cloud.quartz.one.core.dao;

import cn.bootx.starter.quartz.core.entity.QuartzJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**   
* 定时任务
* @author xxm  
* @date 2021/11/2 
*/
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
}
