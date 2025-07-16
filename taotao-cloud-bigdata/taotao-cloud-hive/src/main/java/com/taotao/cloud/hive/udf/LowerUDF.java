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

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 1.继承UDF或者UDAF或者UDTF，实现特定的方法；
 * <p>
 * 2.将写好的类打包为jar，如LowerUDF.jar；
 * <p>
 * 3.进入到Hive shell环境中，输入命令add jar /home/hadoop/LowerUDF.jar注册该jar文件；或者把LowerUDF.jar上传到hdfs，hadoop fs
 * -put LowerUDF.jar /home/hadoop/LowerUDF.jar，再输入命令add jar hdfs://hadoop01:8020/user/home/LowerUDF.jar；
 * <p>
 * 4.为该类起一个别名，create temporary function lower_udf as 'UDF.lowerUDF'；注意，这里UDF只是为这个Hive会话临时定义的；
 * <p>
 * 5.在select中使用lower_udf()；
 * <p>
 * Hive中永久注册UDF create function hive.zodiac as 'mastercom.hive.udf.ZodiacUDF' using jar
 * 'hdfs://192.168.10.2001:8020/script/HiveUDF.jar';
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:16
 */
public class LowerUDF extends UDF {

    public Text evaluate(Text str) {
        // input parameter validate
        if (null == str) {
            return null;
        }
        // validate
        if (StringUtils.isBlank(str.toString())) {
            return null;
        }
        // lower
        return new Text(str.toString().toLowerCase());
    }
}
