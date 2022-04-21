package com.taotao.cloud.stock.biz.interfaces.assembler;

import com.xtoon.cloud.sys.domain.model.log.Log;
import com.xtoon.cloud.sys.domain.model.user.UserName;
import com.xtoon.cloud.sys.dto.LogDTO;

/**
 * 日志Assembler
 *
 * @author shuigedeng
 * @date 2021-06-21
 */
public class LogDTOAssembler {

    public static Log toLog(final LogDTO logDTO) {
        Log log = new Log(null, logDTO.getUserName() == null ? null : new UserName(logDTO.getUserName()), logDTO.getOperation(), logDTO.getMethod(),
                logDTO.getParams(), logDTO.getTime(), logDTO.getIp());

        return log;
    }
}
