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

package com.taotao.cloud.generator.maku.config.template;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.generator.maku.common.exception.ServerException;
import com.taotao.cloud.generator.maku.common.utils.JsonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.util.StreamUtils;

/**
 * 代码生成配置内容
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class GeneratorConfig {
    private String template;

    public GeneratorConfig(String template) {
        this.template = template;
    }

    public GeneratorInfo getGeneratorConfig() {
        // 模板路径，如果不是以/结尾，则添加/
        if (!StrUtil.endWith(template, '/')) {
            template = template + "/";
        }

        // 模板配置文件
        InputStream isConfig = this.getClass().getResourceAsStream(template + "config.json");
        if (isConfig == null) {
            throw new ServerException("模板配置文件，config.json不存在");
        }

        try {
            // 读取模板配置文件
            String configContent = StreamUtils.copyToString(isConfig, StandardCharsets.UTF_8);
            GeneratorInfo generator = JsonUtils.parseObject(configContent, GeneratorInfo.class);
            for (TemplateInfo templateInfo : generator.getTemplates()) {
                // 模板文件
                InputStream isTemplate =
                        this.getClass()
                                .getResourceAsStream(template + templateInfo.getTemplateName());
                if (isTemplate == null) {
                    throw new ServerException("模板文件 " + templateInfo.getTemplateName() + " 不存在");
                }
                // 读取模板内容
                String templateContent =
                        StreamUtils.copyToString(isTemplate, StandardCharsets.UTF_8);

                templateInfo.setTemplateContent(templateContent);
            }
            return generator;
        } catch (IOException e) {
            throw new ServerException("读取config.json配置文件失败");
        }
    }
}
