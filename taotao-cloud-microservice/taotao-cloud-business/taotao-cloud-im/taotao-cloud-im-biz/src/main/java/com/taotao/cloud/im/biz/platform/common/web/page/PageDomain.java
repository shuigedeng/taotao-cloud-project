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

package com.taotao.cloud.im.biz.platform.common.web.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/** 分页数据 */
@Data
@NoArgsConstructor
public class PageDomain {
    /** 当前记录起始索引 */
    private Integer pageNum;
    /** 每页显示记录数 */
    private Integer pageSize;
    /** 排序列 */
    private String orderByColumn;
    /** 排序的方向 "desc" 或者 "asc". */
    private String isAsc;

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StrUtil.toUnderlineCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageStart() {
        return (getPageNum() - 1) * getPageSize();
    }

    public Integer getPageEnd() {
        return getPageStart() + getPageSize();
    }

    /** 记录总数 */
    private Long total;
}
