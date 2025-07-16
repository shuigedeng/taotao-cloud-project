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

package com.taotao.cloud.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * add jar /usr/local/hive_data/hiveext.jar;
 * <p>
 * create temporary function stringext as 'com.taotao.cloud.hive.stringext.StringExt';
 * <p>
 * select id, stringext(name),rl, price from t_order_ext;
 * <p>
 * 自定义函数不失效，则需要将导出的Jar包放到HIVE_HOME/lib目录
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 16:58
 */
public final class StringExt extends UDF {

    public String evaluate(String pnb) {
        return "Hello " + pnb;
    }
}
