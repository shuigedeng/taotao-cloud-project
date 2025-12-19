package com.taotao.cloud.sys.biz.supports.mbg;

import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * AppService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class AppService {

    @Resource
    private AppMapper sysUserMapper;

    public List<App> getSysUserList( String account1 ) {
        SelectStatementProvider select = SqlBuilder.select(AppDynamicSqlSupport.app.allColumns())
                .from(AppDynamicSqlSupport.app)
                .where(AppDynamicSqlSupport.name, isLike(account1).filter(Objects::isNull).map(s -> "%" + s + "%"))
                .and(AppDynamicSqlSupport.version, isEqualTo(0))
                .and(AppDynamicSqlSupport.version, isEqualTo(0))
                .orderBy(AppDynamicSqlSupport.createTime.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.selectMany(select);
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean insert( String account, String name ) {
        App sysUser = new App();
        sysUser.setDelFlag(true);
        return sysUserMapper.insert(sysUser) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete( String account1 ) {
        DeleteStatementProvider del = deleteFrom(AppDynamicSqlSupport.app)
                .where(AppDynamicSqlSupport.name, isEqualTo(account1))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        sysUserMapper.delete(del);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update( String account1, String nickName1, Integer state1 ) {
        UpdateStatementProvider update = SqlBuilder.update(AppDynamicSqlSupport.app)
                .set(AppDynamicSqlSupport.name).equalTo(nickName1)
                .set(AppDynamicSqlSupport.version).equalTo(state1)
                .set(AppDynamicSqlSupport.updateTime).equalTo(LocalDateTime.now())
                .where(AppDynamicSqlSupport.name, isEqualTo(account1))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.update(update) > 0;
    }

    public long getCount() {
        SelectStatementProvider selectStatement = select(count(AppDynamicSqlSupport.app.id))
                .from(AppDynamicSqlSupport.app)
                .where(AppDynamicSqlSupport.version, isEqualTo(0))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return sysUserMapper.count(selectStatement);
    }

}

