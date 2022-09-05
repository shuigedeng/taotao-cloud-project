package com.taotao.cloud.quartz.one.core.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.quartz.core.entity.QuartzJobLog;
import cn.bootx.starter.quartz.param.QuartzJobLogQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**   
*
* @author xxm  
* @date 2022/5/1 
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class QuartzJobLogManager extends BaseManager<QuartzJobLogMapper, QuartzJobLog> {

    /**
     * 分页
     */
    public Page<QuartzJobLog> page(PageParam pageParam, QuartzJobLogQuery query){
        Page<QuartzJobLog> mpPage = MpUtil.getMpPage(pageParam, QuartzJobLog.class);

        return this.lambdaQuery()
                .eq(QuartzJobLog::getClassName,query.getClassName())
                .eq(Objects.nonNull(query.getSuccess()),QuartzJobLog::getSuccess,query.getSuccess())
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
