/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.entity.config.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * IEmailConfigMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-29 09:01:48
 */
@Repository
@Mapper
public interface IEmailConfigMapper extends BaseMapper<EmailConfig> {

}
