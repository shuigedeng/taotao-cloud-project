/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.entity.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IQuartzJobMapper extends BaseMapper<QuartzJob> {

}
