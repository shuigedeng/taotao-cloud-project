package com.taotao.cloud.log.biz.log.core.db.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.starter.audit.log.core.db.dao.OperateLogDbManager;
import cn.bootx.starter.audit.log.core.db.entity.OperateLogDb;
import cn.bootx.starter.audit.log.dto.OperateLogDto;
import cn.bootx.starter.audit.log.param.OperateLogParam;
import cn.bootx.starter.audit.log.service.OperateLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**   
* 操作日志
* @author xxm  
* @date 2021/8/12 
*/
@Slf4j
@Service
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "jdbc",matchIfMissing = true)
@RequiredArgsConstructor
public class OperateLogDbService implements OperateLogService {
    private final OperateLogDbManager operateLogManager;

    /**
     * 添加
     */
    @Async("asyncExecutor")
    @Override
    public void add(OperateLogParam operateLog){
        operateLogManager.save(LogConvert.CONVERT.convert(operateLog));
    }

    /**
     * 获取
     */
    @Override
    public OperateLogDto findById(Long id){
        return operateLogManager.findById(id).map(OperateLogDb::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<OperateLogDto> page(PageParam pageParam, OperateLogParam operateLogParam){
        return MpUtil.convert2DtoPageResult(operateLogManager.page(pageParam,operateLogParam));
    }

    /**
     * 删除
     */
    @Override
    public void delete(Long id){
        operateLogManager.deleteById(id);
    }
}
