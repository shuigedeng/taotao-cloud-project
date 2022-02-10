/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.dto.quartz.QuartzJobDto;
import com.taotao.cloud.sys.api.dto.quartz.QuartzJobQueryCriteria;
import com.taotao.cloud.sys.biz.entity.QuartzJob;
import com.taotao.cloud.sys.biz.entity.ScheduledJob;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

public interface ScheduledJobService extends IService<ScheduledJob> {

}
