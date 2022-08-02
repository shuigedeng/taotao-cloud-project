package com.taotao.cloud.sys.biz.modules.database.service.meta.processor;

import com.sanri.tools.modules.database.service.meta.dtos.Column;
import com.sanri.tools.modules.database.service.meta.dtos.Function;
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
