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

package com.taotao.cloud.generator.generator.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.generator.generator.entity.ClassInfo;
import com.taotao.cloud.generator.generator.entity.ParamInfo;
import com.taotao.cloud.generator.generator.entity.ReturnT;
import com.taotao.cloud.generator.generator.service.TtcGeneratorService;
import com.taotao.cloud.generator.generator.util.MapUtil;
import com.taotao.cloud.generator.generator.util.TableParseUtil;
import com.taotao.cloud.generator.generator.util.ValueUtil;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 代码生成控制器
 * @author zhengkai.blog.csdn.net
 */
@Controller
@Slf4j
public class TtcGeneratorController {
    @Autowired private ValueUtil valueUtil;

    @Autowired private TtcGeneratorService ttcGeneratorService;

    @GetMapping("/")
    public ModelAndView defaultPage() {
        return new ModelAndView("index").addObject("value", valueUtil);
    }

    @GetMapping("/index")
    public ModelAndView indexPage() {
        return new ModelAndView("index").addObject("value", valueUtil);
    }

    @GetMapping("/main")
    public ModelAndView mainPage() {
        return new ModelAndView("main").addObject("value", valueUtil);
    }

    @RequestMapping("/template/all")
    @ResponseBody
    public Result getAllTemplates() throws Exception {
        String templates = ttcGeneratorService.getTemplateConfig();
//        return Result.success().put("templates", templates);
        return Result.success();
    }

    @PostMapping("/code/generate")
    @ResponseBody
    public Result generateCode(@RequestBody ParamInfo paramInfo) throws Exception {
        // log.info(JSON.toJSONString(paramInfo.getOptions()));
        if (StringUtils.isEmpty(paramInfo.getTableSql())) {
            return Result.fail("表结构信息为空");
        }

        // 1.Parse Table Structure 表结构解析
        ClassInfo classInfo = null;
        String dataType = MapUtil.getString(paramInfo.getOptions(), "dataType");
        if ("sql".equals(dataType) || dataType == null) {
            classInfo = TableParseUtil.processTableIntoClassInfo(paramInfo);
        } else if ("json".equals(dataType)) {
            // JSON模式：parse field from json string
            classInfo = TableParseUtil.processJsonToClassInfo(paramInfo);
            // INSERT SQL模式：parse field from insert sql
        } else if ("insert-sql".equals(dataType)) {
            classInfo = TableParseUtil.processInsertSqlToClassInfo(paramInfo);
            // 正则表达式模式（非完善版本）：parse sql by regex
        } else if ("sql-regex".equals(dataType)) {
            classInfo = TableParseUtil.processTableToClassInfoByRegex(paramInfo);
            // 默认模式：default parse sql by java
        }

        // 2.Set the params 设置表格参数

        paramInfo.getOptions().put("classInfo", classInfo);
        paramInfo
                .getOptions()
                .put(
                        "tableName",
                        classInfo == null ? System.currentTimeMillis() : classInfo.getTableName());

        // log the generated table and filed size记录解析了什么表，有多少个字段
        // log.info("generated table :{} , size
        // :{}",classInfo.getTableName(),(classInfo.getFieldList() == null ? "" :
        // classInfo.getFieldList().size()));

        // 3.generate the code by freemarker templates with parameters . Freemarker根据参数和模板生成代码
        Map<String, String> result = ttcGeneratorService.getResultByParams(paramInfo.getOptions());
        //        log.info("result {}",result);
        log.info("table:{} - time:{} ", MapUtil.getString(result, "tableName"), new Date());
//        return Result.success().put("outputJson", result);
        return Result.success();
    }
}
