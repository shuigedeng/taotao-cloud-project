package com.taotao.cloud.log.biz.log.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.starter.audit.log.param.DataVersionLogParam;
import org.springframework.scheduling.annotation.Async;

/**
 * 数据版本日志
 * @author xxm
 * @date 2022/1/10
 */
public interface DataVersionLogService {
    /**
     * 添加
     */
    @Async("asyncExecutor")
    void add(DataVersionLogParam param);

    /**
     * 获取
     */
    DataVersionLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<DataVersionLogDto> page(PageParam pageParam, DataVersionLogParam param);

    /**
     * 删除
     */
    void delete(Long id);
}
