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

package com.taotao.cloud.generator.util;

import java.io.Serializable;
import java.util.List;

/**
 * CxSelect树结构实体类
 *
 * @author ruoyi
 */
public class CxSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据值字段名称
     */
    private String v;

    /**
     * 数据标题字段名称
     */
    private String n;

    /**
     * 子集数据字段名称
     */
    private List<CxSelect> s;

    public CxSelect() {}

    public CxSelect(String v, String n) {
        this.v = v;
        this.n = n;
    }

    public List<CxSelect> getS() {
        return s;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getN() {
        return n;
    }

    public void setS(List<CxSelect> s) {
        this.s = s;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
