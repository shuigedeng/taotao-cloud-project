/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.database.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * MybatisPlus配置类
 *
 * @since 2021/3/16 8:53
 */
@Slf4j
@Configuration
@ComponentScan("workflow")
@MapperScan(basePackages = {"workflow.*.mapper", "workflow.mapper"})
public class MybatisPlusConfig {

    /** 对接数据库的实体层 */
    static final String ALIASES_PACKAGE = "workflow.*.entity";

    @Autowired
    private DataSourceUtil dataSourceUtil;

    @Autowired
    private ConfigValueUtil configValueUtil;

    @Primary
    @Bean(name = "dataSourceSystem")
    public DataSource dataSourceOne(DynamicDataSourceProperties properties, DefaultDataSourceCreator dataSourceCreator)
            throws Exception {
        //        return druidDataSource();
        return dynamicDataSource(properties, dataSourceCreator);
    }

    @Bean(name = "sqlSessionFactorySystem")
    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("dataSourceSystem") DataSource dataSource)
            throws Exception {
        return createSqlSessionFactory(dataSource);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        try {
            // 判断是否多租户
            DbBase dbBase = DbTypeUtil.getDb(dataSourceUtil);
            if (Boolean.parseBoolean(configValueUtil.getMultiTenancy())) {
                DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor =
                        new DynamicTableNameInnerInterceptor();
                HashMap<String, TableNameHandler> map = new HashMap<>(150);
                // 原来采用dbInt的方式，防止失败了，对原始库进行增删改
                /*Connection conn = ConnUtil.getConn(dataSourceUtil,dataSourceUtil.getDbInit());*/
                @Cleanup Connection conn = ConnUtil.getConn(dataSourceUtil);
                List<String> tableNames = new ArrayList<>();
                if (conn != null) {
                    JdbcUtil.queryCustomMods(
                                    dbBase.getSqlBase().getTableListPSD(conn, dataSourceUtil), DbTableModel.class)
                            .forEach(dbTableModel -> {
                                tableNames.add(dbTableModel.getTable().toLowerCase());
                            });
                }
                // 将当前连接库的所有表保存, 在列表中的表才进行切库, 所有表名转小写, 后续比对转小写
                DbBase.dynamicAllTableName = tableNames;
                dynamicTableNameInnerInterceptor.setTableNameHandler(dbBase.getDynamicTableNameHandler());
                interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
            }
            // 新版本分页必须指定数据库，否则分页不生效
            // 不指定会动态生效 多数据源不能指定数据库类型
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return interceptor;
    }

    protected DataSource dynamicDataSource(
            DynamicDataSourceProperties properties, DefaultDataSourceCreator dataSourceCreator) throws Exception {
        if (Boolean.parseBoolean(configValueUtil.getMultiTenancy())) {
            // 多租户安全空库保护（下个版本切库失败，直接中止项目，去除切库保护这个动作。）
            String url = ConnUtil.getUrl(dataSourceUtil, "workflow_protect");
            try {
                ConnUtil.getConn(dataSourceUtil.getUserName(), dataSourceUtil.getPassword(), url)
                        .close();
            } catch (Exception e) {
                throw new Exception("WORKFLOW_PROTECT库异常或不存在,请重新创建多租户初始保护库WORKFLOW_PROTECT库：" + e.getMessage());
            }
        }
        String url = ConnUtil.getUrl(dataSourceUtil);
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUsername(dataSourceUtil.getUserName());
        dataSourceProperty.setPassword(dataSourceUtil.getPassword());
        dataSourceProperty.setUrl(url);
        dataSourceProperty.setDriverClassName(DbTypeUtil.getDb(dataSourceUtil).getDriver());
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        boolean hasPrimary = false;
        for (Map.Entry<String, DataSourceProperty> ds :
                properties.getDatasource().entrySet()) {
            if (ds.getKey().equals(properties.getPrimary())
                    || ds.getKey().startsWith(properties.getPrimary() + "_")
                    || properties.getPrimary().equals(ds.getValue().getPoolName())) {
                hasPrimary = true;
                break;
            }
        }
        if (!hasPrimary) {
            // 未配置多数据源， 从主配置复制数据库配置填充多数据源
            dataSource.addDataSource(properties.getPrimary(), dataSourceCreator.createDataSource(dataSourceProperty));
        }
        DataSource ds = dataSource.getDataSource(properties.getPrimary());

        // oracle参数
        if (DbTypeUtil.getDb(url).getClass() == DbOracle.class) {
            if (ds instanceof ItemDataSource) {
                //                String logonUer = "SYSDBA";
                String logonUer = "Default";
                Properties connProp = DbOracle.setConnProp(
                        logonUer, dataSourceProperty.getUsername(), dataSourceProperty.getPassword());
                ((DruidDataSource) ((ItemDataSource) ds).getDataSource()).setConnectProperties(connProp);
            }
        }
        return dataSource;
    }

    @Bean
    public Advisor myDynamicDatasourceGeneratorAdvisor(DsProcessor dsProcessor) {
        DynamicGeneratorInterceptor interceptor = new DynamicGeneratorInterceptor(true, dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
        return advisor;
    }

    protected DataSource druidDataSource() throws Exception {
        DbBase dbBase = DbTypeUtil.getDb(dataSourceUtil);
        String userName = dataSourceUtil.getUserName();
        String password = dataSourceUtil.getPassword();
        String driver = dbBase.getDriver();
        String url = "";

        if (Boolean.parseBoolean(configValueUtil.getMultiTenancy())) {
            // 多租户安全空库保护（下个版本切库失败，直接中止项目，去除切库保护这个动作。）
            url = ConnUtil.getUrl(dataSourceUtil, "WORKFLOW_PROTECT");
            try {
                ConnUtil.getConn(userName, password, url).close();
            } catch (Exception e) {
                throw new Exception("WORKFLOW_PROTECT库异常或不存在,请重新创建多租户初始保护库WORKFLOW_PROTECT库：" + e.getMessage());
            }
        } else {
            url = ConnUtil.getUrl(dataSourceUtil);
        }

        DruidDataSource dataSource = new DruidDataSource();
        if (dbBase.getClass() == DbOracle.class) {
            // Oracle特殊创建数据源方式
            //            String logonUer = "Default";
            String logonUer = "SYSDBA";
            //            String logonUer = "SYSOPER";
            Properties properties = DbOracle.setConnProp(logonUer, userName, password);
            dataSource.setConnectProperties(properties);
        } else {
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
        }
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    public Resource[] resolveMapperLocations() {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<String> mapperLocations = new ArrayList<>();
        mapperLocations.add("classpath:mapper/*/*.xml");
        mapperLocations.add("classpath:mapper/*/*/*.xml");
        List<Resource> resources = new ArrayList<Resource>();
        for (String mapperLocation : mapperLocations) {
            try {
                Resource[] mappers = resourceResolver.getResources(mapperLocation);
                resources.addAll(Arrays.asList(mappers));
            } catch (IOException e) {
                // ignore
            }
        }
        return resources.toArray(new Resource[0]);
    }

    public SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        // 配置填充器
        globalConfig.setMetaObjectHandler(new MybatisPlusMetaObjectHandler());
        bean.setGlobalConfig(globalConfig);

        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage(ALIASES_PACKAGE);
        bean.setMapperLocations(resolveMapperLocations());
        bean.setConfiguration(configuration(dataSource));
        bean.setPlugins(new Interceptor[] {pageHelper(), new MyMasterSlaveAutoRoutingPlugin(dataSource)});
        return bean.getObject();
    }

    public PageInterceptor pageHelper() {
        PageInterceptor pageHelper = new PageInterceptor();
        // 配置PageHelper参数
        Properties properties = new Properties();
        properties.setProperty("dialectAlias", "kingbasees8=com.github.pagehelper.dialect.helper.MySqlDialect");
        properties.setProperty("autoRuntimeDialect", "true");
        properties.setProperty("offsetAsPageNum", "false");
        properties.setProperty("rowBoundsWithCount", "false");
        properties.setProperty("pageSizeZero", "true");
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "false");
        properties.setProperty("returnPageInfo", "none");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    public MybatisConfiguration configuration(DataSource dataSource) {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(false);
        mybatisConfiguration.setCacheEnabled(false);
        mybatisConfiguration.addInterceptor(mybatisPlusInterceptor());
        mybatisConfiguration.setLogImpl(Slf4jImpl.class);
        mybatisConfiguration.setJdbcTypeForNull(JdbcType.NULL);
        return mybatisConfiguration;
    }

    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return (builderAssistant, mapperClass) -> {};
    }

    /**
     * 数据权限插件
     *
     * @return DataScopeInterceptor
     */
    //    @Bean
    //    @ConditionalOnMissingBean
    //    public DataScopeInterceptor dataScopeInterceptor(DataSource dataSource) {
    //        return new DataScopeInterceptor(dataSource);
    //    }

}
