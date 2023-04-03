package com.taotao.cloud.log.biz.log.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.log.biz.log.dto.DataVersionLogDto;
import com.taotao.cloud.log.biz.log.param.DataVersionLogParam;

/**
 * 数据版本日志
 * @author shuigedeng
 * @date 2022/1/10
 */
public interface DataVersionLogService {
    /**
     * 添加
     */
    void add(DataVersionLogParam param);

    /**
     * 获取
     */
    DataVersionLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<DataVersionLogDto> page(DataVersionLogParam param);

    /**
     * 删除
     */
    void delete(Long id);
}
