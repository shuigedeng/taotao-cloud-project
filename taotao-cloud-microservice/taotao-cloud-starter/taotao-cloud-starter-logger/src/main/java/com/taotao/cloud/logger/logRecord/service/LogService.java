package com.taotao.cloud.logger.logRecord.service;


import com.taotao.cloud.logger.logRecord.bean.LogDTO;

public interface LogService {

    boolean createLog(LogDTO logDTO);

}
