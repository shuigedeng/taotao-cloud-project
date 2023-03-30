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

package com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson;

import java.util.Date;
import lombok.Data;

/** 解析引擎 */
@Data
public class DateProperties {

    /** 定时器* */
    private String title;

    private Integer day = 0;
    private Integer hour = 0;
    private Integer minute = 0;
    private Integer second = 0;
    /** 判断是否有定时器* */
    private Boolean time = false;
    /** 定时器id* */
    private String nodeId;
    /** 定时器下一节点* */
    private String nextId;
    /** 定时任务结束时间* */
    private Date date;
}
