package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Function;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FunctionColumnListProcessor implements ResultSetHandler<List<Function.FunctionColumn>> {
    @Override
    public List<Function.FunctionColumn> handle(ResultSet rs) throws SQLException {
        return null;
    }
}
