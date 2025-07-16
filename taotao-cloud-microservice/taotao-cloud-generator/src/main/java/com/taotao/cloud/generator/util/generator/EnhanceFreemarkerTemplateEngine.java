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

import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.List;
import java.util.Map;

/** 代码生成器支持自定义[DTO\VO等]模版 */
public class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    @Override
    protected void outputCustomFile(
            List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        // String entityName = tableInfo.getEntityName();
        // String otherPath = this.getPathInfo(OutputFile.other);
        // customFile.forEach((key, value) -> {
        //	String fileName = String.format(otherPath + File.separator + entityName + "%s", key);
        //	this.outputFile(new File(fileName), objectMap, value, true);
        // });

        super.outputCustomFile(customFiles, tableInfo, objectMap);
    }
}
