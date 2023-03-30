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

package com.taotao.cloud.workflow.biz.common.database.model;

import java.util.List;
import lombok.Data;

/** jdbc分页模型 */
@Data
public class JdbcPageMod<T> {

    /** 页面大小 */
    private Integer pageSize;

    /** 当前页 */
    private Integer currentPage;

    /** 数据总条数 */
    private Integer totalRecord;

    /** 数据 */
    private List<T> dataList;
}
