package com.taotao.cloud.log.biz.log.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.starter.audit.log.dto.OperateLogDto;
import cn.bootx.starter.audit.log.param.OperateLogParam;
import org.springframework.scheduling.annotation.Async;

/**   
* 操作日志
* @author xxm  
* @date 2021/8/12 
*/
public interface OperateLogService {

    /**
     * 添加
     */
    @Async("asyncExecutor")
    void add(OperateLogParam operateLog);

    /**
     * 获取
     */
    OperateLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<OperateLogDto> page(PageParam pageParam, OperateLogParam operateLogParam);

    /**
     * 删除
     */
    void delete(Long id);
}
