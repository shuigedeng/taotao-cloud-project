package com.taotao.cloud.stock.biz.interfaces.facade;

import com.xtoon.cloud.sys.application.assembler.LogDTOAssembler;
import com.xtoon.cloud.sys.domain.model.log.LogRepository;
import com.xtoon.cloud.sys.dto.LogDTO;
import com.xtoon.cloud.sys.service.LogSaveService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志保存服务实现
 *
 * @author shuigedeng
 * @date 2021-06-21
 */
@DubboService(timeout = 3000)
public class LogSaveServiceImpl implements LogSaveService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void save(LogDTO logDTO) {
        logRepository.store(LogDTOAssembler.toLog(logDTO));
    }
}
