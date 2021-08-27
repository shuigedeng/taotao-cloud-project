package com.taotao.cloud.health.filter;

import com.yh.csx.bsf.core.base.BsfException;
import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

/**
 * @author: chejiangyi
 * @version: 2019-08-02 09:43
 **/
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class,BoundSql.class}),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class }) })
public class SqlMybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 得到sql语句
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql();
        try {
            Object returnObj = Collector.Default.hook("bsf.mybatis.sql.hook").run(StringUtils.nullToEmpty(sql).replace("\r","").replace("\n",""), () -> {
                try {
                    return invocation.proceed();
                } catch (Exception e) {
                    throw new BsfException(e);
                }
            });
            return returnObj;
        }catch (BsfException exp)
        {
            throw exp.getSource();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
