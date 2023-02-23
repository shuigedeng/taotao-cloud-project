/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.demo.core.enums;

import cn.herodotus.engine.assistant.core.definition.enums.BaseUiEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 令牌格式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/25 0:02
 */
@Schema(name = "令牌格式")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TokenFormat implements BaseUiEnum<Integer> {

    /**
     * enum
     */
    SELF_CONTAINED(0,"self-contained", "自包含格式令牌"),
    REFERENCE(1,"reference", "引用（不透明）令牌");

    @Schema(title = "枚举值")
    private final Integer value;
    @Schema(title = "格式")
    private final String format;
    @Schema(title = "文字")
    private final String description;

    private static final Map<Integer, TokenFormat> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    static {
        for (TokenFormat tokenFormat : TokenFormat.values()) {
            INDEX_MAP.put(tokenFormat.getValue(), tokenFormat);
            JSON_STRUCTURE.add(tokenFormat.getValue(),
                    ImmutableMap.<String, Object>builder()
                            // 使用数字作为 value, 适用于单选，同时数据库只存 value值即可
                            // 使用具体的字符串值作为value, 适用于多选，同时数据库存储以逗号分隔拼接的字符串
                            .put("value", tokenFormat.getValue())
                            .put("key", tokenFormat.name())
                            .put("text", tokenFormat.getDescription())
                            .put("format", tokenFormat.getFormat())
                            .put("index", tokenFormat.ordinal())
                            .build());
        }
    }

    TokenFormat(Integer value, String method, String description) {
        this.value = value;
        this.format = method;
        this.description = description;
    }

    /**
     * 不加@JsonValue，转换的时候转换出完整的对象。
     * 加了@JsonValue，只会显示相应的属性的值
     * <p>
     * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
     *
     * @return Enum枚举值
     */
    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static TokenFormat get(Integer index) {
        return INDEX_MAP.get(index);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }
}
