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

package com.taotao.cloud.workflow.biz.flowable.flowable.domain.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Tony
 * @since 2021/3/28 19:48
 */
@Data
public class FlowSaveXmlVo implements Serializable {

    /** 流程名称 */
    private String name;

    /** 流程分类 */
    private String category;

    /** xml 文件 */
    private String xml;
}
