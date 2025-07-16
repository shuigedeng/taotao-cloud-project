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

package com.taotao.cloud.generator.maku.utils;

import cn.hutool.core.io.IoUtil;
import com.taotao.cloud.generator.maku.common.exception.ServerException;
import freemarker.template.Template;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板工具类
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Slf4j
public class TemplateUtils {
    /**
     * 获取模板渲染后的内容
     *
     * @param content   模板内容
     * @param dataModel 数据模型
     */
    public static String getContent(String content, Map<String, Object> dataModel) {
        if (dataModel.isEmpty()) {
            return content;
        }

        StringReader reader = new StringReader(content);
        StringWriter sw = new StringWriter();
        try {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, sw);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerException("渲染模板失败，请检查模板语法", e);
        }

        content = sw.toString();

        IoUtil.close(reader);
        IoUtil.close(sw);

        return content;
    }
}
