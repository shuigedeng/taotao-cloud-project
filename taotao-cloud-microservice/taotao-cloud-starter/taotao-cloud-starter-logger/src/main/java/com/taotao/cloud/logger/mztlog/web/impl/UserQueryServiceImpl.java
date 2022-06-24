package com.taotao.cloud.logger.mztlog.web.impl;

import com.taotao.cloud.logger.mztlog.context.LogRecordContext;
import com.taotao.cloud.logger.mztlog.starter.annotation.LogRecord;
import com.taotao.cloud.logger.mztlog.web.UserQueryService;
import com.taotao.cloud.logger.mztlog.web.infrastructure.constants.LogRecordType;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Override
    @LogRecord(success = "获取用户列表,内层方法调用人{{#user}}", type = LogRecordType.ORDER, bizNo = "MT0000011")
    public List<User> getUserList(List<String> userIds) {
        LogRecordContext.putVariable("user", "mzt");
        return null;
    }
}
