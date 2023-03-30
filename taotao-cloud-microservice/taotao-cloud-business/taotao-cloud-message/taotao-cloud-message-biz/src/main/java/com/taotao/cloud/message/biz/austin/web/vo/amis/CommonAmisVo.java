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

package com.taotao.cloud.message.biz.austin.web.vo.amis;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * amis的通用转化类
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonAmisVo {

    private String type;
    private String id;

    private String label;

    private String value;
    private String schemaApi;

    private String mode;
    private String name;
    private boolean fixedSize;
    private String fixedSizeClassName;
    private String frameImage;
    private String originalSrc;
    private Integer interval;

    private boolean required;
    private boolean silentPolling;

    private String size;
    private String target;

    private boolean addable;

    private boolean editable;

    private boolean needConfirm;

    private String width;

    private String height;

    private String src;

    private String title;

    private String imageMode;

    private String varParam;

    private List<CommonAmisVo> body;

    private ApiDTO api;
    /** columns */
    @JSONField(name = "columns")
    private List<ColumnsDTO> columns;

    /** ColumnsDTO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ColumnsDTO {

        /** nameX */
        @JSONField(name = "name")
        private String name;
        /** labelX */
        @JSONField(name = "label")
        private String label;

        /** type */
        @JSONField(name = "type")
        private String type;
        /** placeholder */
        @JSONField(name = "placeholder")
        private String placeholder;

        /** type */
        @JSONField(name = "required")
        private Boolean required;

        @JSONField(name = "quickEdit")
        private Boolean quickEdit;
    }

    /** ApiDTO */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ApiDTO {

        /** adaptor */
        @JSONField(name = "adaptor")
        private String adaptor;

        /** adaptor */
        @JSONField(name = "requestAdaptor")
        private String requestAdaptor;

        /** url */
        @JSONField(name = "url")
        private String url;
    }
}
