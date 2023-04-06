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

package com.taotao.cloud.workflow.biz.common.base;

import java.util.List;
import lombok.Data;

/** */
@Data
public class PageModel {
    /** 每页行数 */
    private int rows;
    /** 当前页 */
    private int page;
    /** 总页数 */
    private long total;
    /** 总记录数 */
    private long records;
    /** 排序列 */
    private String sidx;
    /** 排序类型 */
    private String sord;
    /** 查询条件 */
    private String queryJson;
    /** 查询关键字 */
    private String keyword;

    public <T> List<T> setData(List<T> data, long records) {
        this.records = records;
        if (this.records > 0) {
            this.total = this.records % this.rows == 0 ? this.records / this.rows : this.records / this.rows + 1;
        } else {
            this.total = 0;
        }
        return data;
    }
}
