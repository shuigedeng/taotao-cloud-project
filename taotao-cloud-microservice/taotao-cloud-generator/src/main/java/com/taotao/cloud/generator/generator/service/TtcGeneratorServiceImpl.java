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

package com.taotao.cloud.generator.generator.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.generator.generator.util.FreemarkerUtil;
import com.taotao.cloud.generator.generator.util.MapUtil;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * GeneratorService
 *
 * @author zhengkai.blog.csdn.net
 */
@Slf4j
@Service
public class TtcGeneratorServiceImpl implements TtcGeneratorService {

    String templateCpnfig = null;

    /**
     * 从项目中的JSON文件读取String
     *
     * @author zhengkai.blog.csdn.net
     */
    @Override
    public String getTemplateConfig() throws IOException {
        templateCpnfig = null;
        if (templateCpnfig != null) {
        } else {
            InputStream inputStream =
                    this.getClass().getClassLoader().getResourceAsStream("template.json");
            templateCpnfig =
                    new BufferedReader(new InputStreamReader(inputStream))
                            .lines()
                            .collect(Collectors.joining(System.lineSeparator()));
            inputStream.close();
        }
        // log.info(JSON.toJSONString(templateCpnfig));
        return templateCpnfig;
    }

    /**
     * 根据配置的Template模板进行遍历解析，得到生成好的String
     *
     * @author zhengkai.blog.csdn.net
     */
    @Override
    public Map<String, String> getResultByParams(Map<String, Object> params)
            throws IOException, TemplateException {
        Map<String, String> result = new HashMap<>(32);
        result.put("tableName", MapUtil.getString(params, "tableName"));
        JSONArray parentTemplates = JSONArray.parseArray(getTemplateConfig());
        for (int i = 0; i < parentTemplates.size(); i++) {
            JSONObject parentTemplateObj = parentTemplates.getJSONObject(i);
            for (int x = 0; x < parentTemplateObj.getJSONArray("templates").size(); x++) {
                JSONObject childTemplate =
                        parentTemplateObj.getJSONArray("templates").getJSONObject(x);
                result.put(
                        childTemplate.getString("name"),
                        FreemarkerUtil.processString(
                                parentTemplateObj.getString("group")
                                        + "/"
                                        + childTemplate.getString("name")
                                        + ".ftl",
                                params));
            }
        }
        return result;
    }
}
