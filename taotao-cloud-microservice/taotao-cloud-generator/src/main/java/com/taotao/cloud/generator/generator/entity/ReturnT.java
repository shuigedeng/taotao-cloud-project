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

package com.taotao.cloud.generator.generator.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * common returnT:公共返回封装类
 *
 * @author zhengkai.blog.csdn.net
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnT extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public ReturnT() {
        put("code", 0);
        put("msg", "success");
    }

    public static ReturnT error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ReturnT error(String msg) {
        return error(500, msg);
    }

    public static ReturnT error(int code, String msg) {
        ReturnT r = new ReturnT();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ReturnT define(int code, String msg) {
        ReturnT r = new ReturnT();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ReturnT ok(String msg) {
        ReturnT r = new ReturnT();
        r.put("msg", msg);
        return r;
    }

    public static ReturnT ok(Map<String, Object> map) {
        ReturnT r = new ReturnT();
        r.putAll(map);
        return r;
    }

    public static ReturnT ok() {
        return new ReturnT();
    }

    @Override
    public ReturnT put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
