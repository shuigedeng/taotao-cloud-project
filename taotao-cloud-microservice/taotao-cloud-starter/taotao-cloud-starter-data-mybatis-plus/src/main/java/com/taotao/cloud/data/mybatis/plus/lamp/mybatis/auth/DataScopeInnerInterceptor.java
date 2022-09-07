package com.taotao.cloud.data.mybatis.plus.lamp.mybatis.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import top.tangyh.basic.context.ContextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.tangyh.basic.model.database.DataScope;
import top.tangyh.basic.model.database.DataScopeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * mybatis 数据权限拦截器
 * 联系作者购买企业版后，获取完整功能源码
 * <p>
 *
 * @author zuihou
 * @date 2020/9/27 10:00 上午
 */
@Slf4j
@AllArgsConstructor
public class DataScopeInnerInterceptor implements InnerInterceptor {
    private final Function<Long, Map<String, Object>> function;


    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    protected Optional<DataScope> findDataScope(Object parameterObj) {
        if (parameterObj == null) {
            return Optional.empty();
        }
        if (parameterObj instanceof DataScope) {
            return Optional.of((DataScope) parameterObj);
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return Optional.of((DataScope) val);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        DataScope dataScope = findDataScope(parameter).orElse(null);
        if (dataScope == null) {
            return;
        }
        // 联系作者购买企业版后，获取完整功能源码
        String originalSql = boundSql.getSql();

        String scopeName = dataScope.getScopeName();
        String selfScopeName = dataScope.getSelfScopeName();
        Long userId = dataScope.getUserId() == null ? ContextUtil.getUserId() : dataScope.getUserId();
        List<Long> orgIds = dataScope.getOrgIds();
        DataScopeType dsType = DataScopeType.SELF;
        if (CollectionUtil.isEmpty(orgIds)) {
            //查询当前用户的 角色 最小权限
            //userId

            //dsType orgIds
            Map<String, Object> result = function.apply(userId);
            if (result == null) {
                return;
            }

            Integer type = (Integer) result.get("dsType");
            dsType = DataScopeType.get(type);
            orgIds = (List<Long>) result.get("orgIds");
        }

        //查全部
        if (DataScopeType.ALL.eq(dsType)) {
            return;
        }
        //查个人
        if (DataScopeType.SELF.eq(dsType)) {
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + selfScopeName + " = " + userId;
        }
        //查其他
        else if (StrUtil.isNotBlank(scopeName) && CollUtil.isNotEmpty(orgIds)) {
            String join = CollectionUtil.join(orgIds, ",");
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
        }

        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        mpBoundSql.sql(originalSql);
    }

}
