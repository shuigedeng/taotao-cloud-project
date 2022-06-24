package com.taotao.cloud.logger.mztlog.service.impl;

import com.google.common.collect.Lists;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.mztlog.beans.LogRecord;
import com.taotao.cloud.logger.mztlog.service.ILogRecordService;

import java.util.List;
import java.util.Optional;

public class DefaultLogRecordServiceImpl implements ILogRecordService {

//    @Resource
//    private LogRecordMapper logRecordMapper;

    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(LogRecord logRecord) {
        LogUtil.info("【logRecord】log={}", logRecord);
        //throw new RuntimeException("sss");
//        logRecordMapper.insertSelective(logRecord);
    }

    @Override
    public void batchRecord(List<LogRecord> records) {
        Optional.ofNullable(records).ifPresent(x -> x.forEach(y ->
			LogUtil.info("【logRecord】log={}", y)));
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return Lists.newArrayList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return Lists.newArrayList();
    }


}
