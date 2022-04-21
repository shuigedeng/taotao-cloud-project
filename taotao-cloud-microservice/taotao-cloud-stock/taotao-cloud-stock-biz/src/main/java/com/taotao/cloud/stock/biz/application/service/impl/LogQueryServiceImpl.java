package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xtoon.cloud.common.mybatis.util.Page;
import com.xtoon.cloud.common.mybatis.util.PageAssembler;
import com.xtoon.cloud.common.mybatis.util.Query;
import com.xtoon.cloud.sys.application.LogQueryService;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysLogDO;
import com.xtoon.cloud.sys.infrastructure.persistence.mapper.SysLogMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 系统日志查询服务实现类
 *
 * @author shuigedeng
 * @date 2021-05-10
 */
@Service
public class LogQueryServiceImpl implements LogQueryService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Page queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<SysLogDO> page = sysLogMapper.selectPage(
                new Query<SysLogDO>().getPage(params),
                new QueryWrapper<SysLogDO>().like(StringUtils.isNotBlank(key), "username", key)
        );
        return PageAssembler.toPage(page);
    }

}
