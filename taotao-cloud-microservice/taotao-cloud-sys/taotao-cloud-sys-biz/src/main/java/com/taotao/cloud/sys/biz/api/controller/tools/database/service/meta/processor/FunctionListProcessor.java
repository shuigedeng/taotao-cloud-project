package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FunctionListProcessor implements ResultSetHandler<List<Function>>{
    @Override
    public List<Function> handle(ResultSet rs) throws SQLException {
        return null;
    }
}
