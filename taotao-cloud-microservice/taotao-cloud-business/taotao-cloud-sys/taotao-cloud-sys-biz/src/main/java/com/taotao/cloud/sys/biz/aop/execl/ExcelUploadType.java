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

package com.taotao.cloud.sys.biz.aop.execl;

import java.util.HashMap;
import java.util.Map;

public enum ExcelUploadType {
    未知(1, "未知"),
    类型2(2, "类型2"),
    类型1(3, "类型1");

    private int code;
    private String desc;
    private static Map<Integer, ExcelUploadType> map = new HashMap<>();

    static {
        for (ExcelUploadType value : ExcelUploadType.values()) {
            map.put(value.code, value);
        }
    }

    ExcelUploadType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ExcelUploadType getByCode(Integer code) {
        return map.get(code);
    }
}
