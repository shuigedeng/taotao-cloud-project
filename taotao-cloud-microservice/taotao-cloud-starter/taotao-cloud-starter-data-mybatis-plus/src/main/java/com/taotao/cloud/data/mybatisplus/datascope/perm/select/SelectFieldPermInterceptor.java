package com.taotao.cloud.data.mybatisplus.datascope.perm.select;

import cn.bootx.common.core.annotation.Permission;
import cn.bootx.common.core.entity.UserDetail;
import cn.bootx.starter.data.perm.configuration.DataPermProperties;
import cn.bootx.starter.data.perm.exception.NotLoginPermException;
import cn.bootx.starter.data.perm.local.DataPermContextHolder;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
* 查询字段权限拦截器
* @author xxm
* @date 2021/12/21
*/
@Component
@RequiredArgsConstructor
public class SelectFieldPermInterceptor  extends JsqlParserSupport implements InnerInterceptor {
    private final DataPermProperties dataPermProperties;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql){
        // 配置是否开启了权限控制
        if (!dataPermProperties.isEnableSelectFieldPerm()){
            return;
        }
        // 是否添加了对应的注解来开启数据权限控制
        Permission permission = DataPermContextHolder.getPermission();
        if (Objects.isNull(permission) || !permission.selectField()){
            return;
        }
        // 检查是否已经登录和是否是超级管理员
        boolean admin = DataPermContextHolder.getUserDetail()
                .map(UserDetail::isAdmin)
                .orElseThrow(NotLoginPermException::new);
        // 是否超级管理员
        if (admin){
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        // 解析器
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect plainSelect) {
			List<SelectItem> selectItems = plainSelect.getSelectItems();

            plainSelect.setSelectItems(selectItems);
        }
    }
}
