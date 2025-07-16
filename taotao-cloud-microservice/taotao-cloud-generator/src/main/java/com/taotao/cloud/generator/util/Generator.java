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

package com.taotao.cloud.generator.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.taotao.boot.data.jpa.tenant.BaseEntity;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Create by whz at 2023/8/6
 */
public class Generator {

    // 数据库连接配置
    private static final String JDBC_URL =
            "jdbc:mysql://weihuazhou.top:3306/wonder?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static final String JDBC_USER_NAME = "root";
    private static final String JDBC_PASSWORD = "5278eedc6119a326";

    // 包名和模块名
    private static final String PACKAGE_NAME = "com.gitee.whzzone.admin.business";
    private static final String MODULE_NAME = "";

    // 表名，多个表使用英文逗号分割
    private static final String[] TBL_NAMES = {"ex_book"};

    // 表名的前缀，从表生成代码时会去掉前缀
    private static final String TABLE_PREFIX = "ex_";

    // 生成代码入口main方法
    public static void main(String[] args) {
        // 1.数据库配置
        DataSourceConfig.Builder dataSourceConfigBuilder =
                new DataSourceConfig.Builder(JDBC_URL, JDBC_USER_NAME, JDBC_PASSWORD)
                        .dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler());

        // 1.1.快速生成器
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceConfigBuilder);

        // 2.全局配置
        // 覆盖已生成文件
        // 不打开生成文件目录
        // 指定输出目录,注意使用反斜杠\
        // 设置注释的作者
        // 设置注释的日期格式
        // 使用java8新的时间类型
        fastAutoGenerator.globalConfig(
                globalConfigBuilder ->
                        globalConfigBuilder
                                // .fileOverride()
                                .disableOpenDir()
                                .enableSwagger()
                                .outputDir("wonder-admin\\src\\main\\java")
                                .author("generator")
                                .commentDate("yyyy/M/d")
                                .dateType(DateType.ONLY_DATE));

        // 3.包配置
        // 设置父包名
        // 设置父包模块名
        // 设置MVC下各个模块的包名
        // 设置XML资源文件的目录
        fastAutoGenerator.packageConfig(
                packageConfigBuilder ->
                        packageConfigBuilder
                                .parent(PACKAGE_NAME)
                                // .moduleName(MODULE_NAME)
                                .entity(MODULE_NAME.isEmpty() ? "entity" : "entity." + MODULE_NAME)
                                .mapper(MODULE_NAME.isEmpty() ? "mapper" : "mapper." + MODULE_NAME)
                                .service(
                                        MODULE_NAME.isEmpty()
                                                ? "service"
                                                : "service." + MODULE_NAME)
                                .serviceImpl(
                                        MODULE_NAME.isEmpty()
                                                ? "service.impl"
                                                : "service." + MODULE_NAME + ".impl")
                                .controller(
                                        MODULE_NAME.isEmpty()
                                                ? "controller"
                                                : "controller." + MODULE_NAME)
                                //                .other(MODULE_NAME.isEmpty() ? "pojo.other" :
                                // "pojo." + MODULE_NAME + "other")
                                .pathInfo(
                                        Collections.singletonMap(
                                                OutputFile.xml,
                                                MODULE_NAME.isEmpty()
                                                        ? "wonder-admin\\src\\main\\resources\\mapper\\business\\"
                                                        : "wonder-admin\\src\\main\\resources\\mapper\\business\\"
                                                                + MODULE_NAME
                                                                + "\\")));

        fastAutoGenerator.templateConfig(
                templateConfigBuilder ->
                        templateConfigBuilder
                                .entity("/templates/entity.java")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .mapper("/templates/mapper.java")
                                .xml("/templates/mapper.xml")
                                .controller("/templates/controller.java"));

        // 4.模板配置
        // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        AbstractTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        fastAutoGenerator.templateEngine(templateEngine);

        // 5.注入配置-自定义
        fastAutoGenerator.injectionConfig(
                injectionConfig -> {
                    HashMap<String, String> customFileMap = new HashMap<>();

                    injectionConfig.beforeOutputFile(
                            (tableInfo, biConsumer) -> {
                                customFileMap.put(
                                        tableInfo.getEntityName() + "Dto.java",
                                        "/templates/dto.java.ftl");
                                customFileMap.put(
                                        tableInfo.getEntityName() + "Query.java",
                                        "/templates/query.java.ftl");
                            });

                    injectionConfig.customFile(customFileMap);
                });

        // 6.策略配置
        // 设置需要生成的表名
        // 设置过滤表前缀
        fastAutoGenerator.strategyConfig(
                strategyConfigBuilder ->
                        strategyConfigBuilder
                                .enableCapitalMode()
                                .enableSkipView()
                                .disableSqlFilter()
                                .addInclude(TBL_NAMES)
                                .addTablePrefix(TABLE_PREFIX));

        // 6.1.Entity策略配置
        // 生成实体时生成字段的注解，包括@TableId注解等
        // 数据库表和字段映射到实体的命名策略，为下划线转驼峰
        // 实体名称格式化为%sEntity
        fastAutoGenerator.strategyConfig(
                strategyConfigBuilder ->
                        strategyConfigBuilder
                                .entityBuilder()
                                .enableTableFieldAnnotation()
                                .naming(NamingStrategy.underline_to_camel)
                                .columnNaming(NamingStrategy.underline_to_camel)
                                .formatFileName("%s")
                                .superClass(BaseEntity.class)
                                .enableLombok()
                                .disableSerialVersionUID());

        // 6.2.Controller策略配置
        // 开启生成@RestController控制器
        fastAutoGenerator.strategyConfig(
                strategyConfigBuilder -> strategyConfigBuilder.controllerBuilder().enableRestStyle()
                //                .superClass(EntityController.class)
                );

        // 6.3.Service策略配置
        // 格式化service接口和实现类的文件名称，去掉默认的ServiceName前面的I
        fastAutoGenerator.strategyConfig(
                strategyConfigBuilder ->
                        strategyConfigBuilder
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                                //                .superServiceClass(EntityService.class)
                                .formatServiceImplFileName("%sServiceImpl")
                //                .superServiceImplClass(EntityServiceImpl.class)
                );

        // 6.4.Mapper策略配置
        // 格式化 mapper文件名,格式化xml实现类文件名称
        fastAutoGenerator.strategyConfig(
                strategyConfigBuilder ->
                        strategyConfigBuilder
                                .mapperBuilder()
                                .formatMapperFileName("%sMapper")
                                .formatXmlFileName("%sMapper")
                                .enableMapperAnnotation()
                                .enableBaseResultMap()
                                .enableBaseColumnList());

        // 7.生成代码
        fastAutoGenerator.execute();
    }
}
