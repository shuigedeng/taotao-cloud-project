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

package com.taotao.cloud.generator.util.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** 代码生成器 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 数据源配置
        FastAutoGenerator.create(
                        "jdbc:mysql://127.0.0.1:3306/sharding-db0?serverTimezone=GMT%2B8",
                        "root", "admin")
                .dataSourceConfig(
                        builder -> {
                            builder.dbQuery(new MySqlQuery())
                                    .schema("mybatis-plus")
                                    .typeConvert(new MySqlTypeConvert())
                                    .keyWordsHandler(new MySqlKeyWordsHandler());
                        })
                .globalConfig(
                        builder -> {
                            builder.author("austin") // 设置作者
                                    .enableSwagger() // 开启 swagger 模式 默认值:false
                                    .disableOpenDir() // 禁止打开输出目录 默认值:true
                                    .commentDate("yyyy-MM-dd") // 注释日期
                                    // 定义生成的实体类中日期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                                    .dateType(DateType.TIME_PACK)
                                    .outputDir(
                                            System.getProperty("user.dir")
                                                    + "/src/main/java"); // 指定输出目录
                        })
                .packageConfig(
                        builder -> {
                            builder.parent("com.taotao.cloud.sys.biz") // 父包模块名
                                    .controller("controller") // Controller 包名 默认值:controller
                                    .entity("entity") // Entity 包名 默认值:entity
                                    .service("service") // Service 包名 默认值:service
                                    .mapper("mapper") // Mapper 包名 默认值:mapper
                                    // .moduleName("xxx")        // 设置父包模块名 默认值:无
                                    .pathInfo(
                                            Collections.singletonMap(
                                                    OutputFile.xml,
                                                    System.getProperty("user.dir")
                                                            + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                            // 默认存放在mapper的xml下
                        })
                .injectionConfig(
                        consumer -> {
                            Map<String, String> customFile = new HashMap<>();
                            // DTO、VO MapStruct
                            customFile.put("DTO.java", "/generator/entityDTO.java.ftl");
                            customFile.put("VO.java", "/generator/entityVO.java.ftl");
                            customFile.put("Converter.java", "generator/entityConverter.java.ftl");
                            consumer.customFile(customFile);

                            consumer.beforeOutputFile(
                                    (tableInfo, objectMap) -> {
                                        LogUtils.info(
                                                "tableInfo: "
                                                        + tableInfo.getEntityName()
                                                        + " objectMap: "
                                                        + objectMap.size());
                                    });
                        })
                .strategyConfig(
                        builder -> {
                            builder.addInclude("user") // 设置需要生成的表名 可边长参数“user”, “user1”
                                    .addTablePrefix("tb_", "gms_") // 设置过滤表前缀
                                    .serviceBuilder() // service策略配置
                                    .formatServiceFileName("%sService")
                                    .formatServiceImplFileName("%sServiceImpl")
                                    .entityBuilder() // 实体类策略配置
                                    .idType(IdType.ASSIGN_ID) // 主键策略  雪花算法自动生成的id
                                    .addTableFills(
                                            new Column("create_time", FieldFill.INSERT)) // 自动填充配置
                                    .addTableFills(
                                            new Property("update_time", FieldFill.INSERT_UPDATE))
                                    .enableLombok() // 开启lombok
                                    .logicDeleteColumnName("deleted") // 说明逻辑删除是哪个字段
                                    .enableTableFieldAnnotation() // 属性加上注解说明
                                    .controllerBuilder() // controller 策略配置
                                    .formatFileName("%sController")
                                    .enableRestStyle() // 开启RestController注解
                                    .mapperBuilder() // mapper策略配置
                                    .formatMapperFileName("%sMapper")
                                    .mapperAnnotation(
                                            org.apache.ibatis.annotations.Mapper
                                                    .class) // @mapper注解开启
                                    .formatXmlFileName("%sMapper");
                        })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                // .templateEngine(new FreemarkerTemplateEngine())
                .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();
    }
}
