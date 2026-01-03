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

package com.taotao.cloud.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import lombok.*;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * MyBatis Plus Generator
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
class MyBatisPlusGeneratorTests {

    /**
     * 数据源
     *
     * @author xuxiaowei
     * @since 0.0.1
     */
    @Getter
    private enum DataSource {

        /**
         * 微服务
         */
        XUXIAOWEI_CLOUD(
                "jdbc:mysql://127.0.0.1:3306/xuxiaowei_cloud_next", "root", "root", "微服务 数据库"),

        /**
         * 微服务-日志
         */
        XUXIAOWEI_CLOUD_LOG(
                "jdbc:mysql://127.0.0.1:3306/xuxiaowei_cloud_next_log",
                "root",
                "root",
                "微服务 日志 数据库"),
        ;

        private final String url;

        private final String username;

        private final String password;

        private final String explain;

        DataSource( String url, String username, String password, String explain ) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.explain = explain;
        }
    }

    public static void main( String[] args ) {

        DataSource[] values = DataSource.values();

        for (int i = 0; i < values.length; i++) {
            DataSource value = values[i];
            System.out.printf(
                    "数据库序号：%s 数据库说明：%s 数据库连接串：%s 数据库用户名：%s%n",
                    i, value.explain, value.url, value.username);
        }

        String dataSourceScanner = scanner("请选择数据库序号");
        int dataSourceNum;

        try {
            int integer = Integer.parseInt(dataSourceScanner);

            if (integer >= 0 && integer < values.length) {
                dataSourceNum = integer;
            } else {
                System.err.println("输入数据库序号不在有效范围内");
                main(args);
                return;
            }

        } catch (Exception e) {
            System.err.println("输入数据库序号不正确");
            LogUtils.error(e);
            main(args);
            return;
        }

        DataSource dataSource = values[dataSourceNum];

        DataSourceConfig.Builder dataSourceConfig =
                new DataSourceConfig.Builder(
                        dataSource.url, dataSource.username, dataSource.password)
                        .dbQuery(new MySqlQuery())
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler());

        String userDir = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        List<Module> moduleList =
                Arrays.asList(
                        new Module("gateway", "cloud.xuxiaowei.next.gateway"),
                        new Module(
                                "cloud-commons-parent" + fileSeparator + "cloud-starter-core",
                                "cloud.xuxiaowei.next.core"),
                        new Module(
                                "cloud-commons-parent" + fileSeparator + "cloud-starter-log",
                                "cloud.xuxiaowei.next.log"),
                        new Module(
                                "cloud-commons-parent" + fileSeparator + "cloud-starter-oauth2",
                                "cloud.xuxiaowei.next.oauth2"),
                        new Module(
                                "cloud-commons-parent" + fileSeparator + "cloud-starter-system",
                                "cloud.xuxiaowei.next.system"),
                        new Module("passport", "cloud.xuxiaowei.next.passport"),
                        new Module(
                                "resource-services-parent" + fileSeparator + "wechat-miniprogram",
                                "cloud.xuxiaowei.next.wechatminiprogram"));

        LogUtils.info("项目文件夹：" + userDir);

        LogUtils.info("模块列表：");
        for (int i = 0; i < moduleList.size(); i++) {
            Module module = moduleList.get(i);
            LogUtils.info(
                    "序号：" + i + "：" + module.getModuleFolder() + "：" + module.getPackageName());
        }

        int moduleNumber = scannerInt("请输入模块名序号？");
        Module module = moduleList.get(moduleNumber);
        String moduleFolder = module.getModuleFolder();
        String packageName = module.getPackageName();

        String[] packageNameSplit = packageName.split("\\.");
        String xmlFolder = packageNameSplit[packageNameSplit.length - 1];

        String mainFolder =
                userDir
                        + fileSeparator
                        + moduleFolder
                        + fileSeparator
                        + "src"
                        + fileSeparator
                        + "main";

        String javaDir = mainFolder + fileSeparator + "java";
        String xmlDir =
                mainFolder
                        + fileSeparator
                        + "resources"
                        + fileSeparator
                        + "mapper"
                        + fileSeparator
                        + xmlFolder;

        LogUtils.info("java 输出目录：" + javaDir);
        LogUtils.info("xml 输出目录：" + xmlDir);

        FastAutoGenerator.create(dataSourceConfig)
                // 全局配置
                .globalConfig(
                        ( scanner, builder ) -> {
                            builder.author(scanner.apply("请输入作者名称？"));

                            // 禁止打开输出目录
                            builder.disableOpenDir();
                            // 输出目录
                            builder.outputDir(javaDir);
                        })
                // 包配置
                .packageConfig(
                        ( scanner, builder ) -> {
                            builder.parent(packageName);

                            builder.pathInfo(Collections.singletonMap(OutputFile.xml, xmlDir));
                        })
                // 策略配置
                .strategyConfig(
                        ( scanner, builder ) ->
                                builder.addInclude(
                                                getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                                        .controllerBuilder()
                                        // 开启生成@RestController控制器
                                        .enableRestStyle()
                                        // 开启驼峰转连字符
                                        .enableHyphenStyle()
                                        .mapperBuilder()
                                        // 开启baseResultMap
                                        .enableBaseResultMap()
                                        .entityBuilder()
                                        // 开启lombok模型
                                        .enableLombok()
                                        .addTableFills(
                                                new Column("create_date", FieldFill.INSERT),
                                                new Column("update_date", FieldFill.UPDATE),
                                                new Column("create_username", FieldFill.INSERT),
                                                new Column("update_username", FieldFill.UPDATE),
                                                new Column("create_ip", FieldFill.INSERT),
                                                new Column("update_ip", FieldFill.UPDATE))
                                        .logicDeleteColumnName("deleted")
                                        .build())
                /*
                 * 模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker .templateEngine(new
                 * BeetlTemplateEngine()) .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();
    }

    /**
     * Module
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    @Data
    @AllArgsConstructor
    private static class Module {

        private String moduleFolder;

        private String packageName;
    }

    /**
     * 读取控制台内容
     */
    private static String scanner( String tip ) {
        Scanner scanner = new Scanner(System.in);
        LogUtils.info(( "请输入" + tip + "：" ));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 读取控制台内容
     */
    private static int scannerInt( String tip ) {
        Scanner scanner = new Scanner(System.in);
        LogUtils.info(( "请输入" + tip + "：" ));
        if (scanner.hasNext()) {
            return scanner.nextInt();
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 处理 all 情况
     *
     * @param tables 表
     * @return 返回表名
     */
    private static List<String> getTables( String tables ) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
