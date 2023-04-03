package com.taotao.cloud.log.biz.log.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import org.springframework.scheduling.annotation.Async;

/**   
* 操作日志
* @author shuigedeng  
* @date 2021/8/12 
*/
public interface OperateLogService {

    /**
     * 添加
     */
    void add(OperateLogParam operateLog);

    /**
     * 获取
     */
    OperateLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<OperateLogDto> page( OperateLogParam operateLogParam);

    /**
     * 删除
     */
    void delete(Long id);
}
