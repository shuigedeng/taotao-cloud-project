package com.taotao.cloud.stock.biz.domain.log.repository;

import com.taotao.cloud.stock.biz.domain.model.log.Log;
import com.taotao.cloud.stock.biz.domain.log.model.entity.Log;

/**
 * 日志-Repository接口
 *
 * @author haoxin
 * @date 2021-02-02
 **/
public interface
LogRepository {

    /**
     * 保存日志
     *
     * @param log
     */
    void store(Log log);
}
