package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.processor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseableResultSetHandler<T> implements ResultSetHandler<T> {
    private ResultSetHandler<T> resultSetHandler;

    public CloseableResultSetHandler(ResultSetHandler<T> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        assert resultSetHandler != null;
        try {
           return resultSetHandler.handle(rs);
        }finally {
            DbUtils.close(rs);
        }
    }
}
