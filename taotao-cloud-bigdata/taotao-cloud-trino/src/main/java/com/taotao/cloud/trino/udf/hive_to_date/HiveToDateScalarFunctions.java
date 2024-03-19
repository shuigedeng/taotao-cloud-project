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

package com.taotao.cloud.trino.udf.hive_to_date;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.spi.function.Description;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:36
 */
public class HiveToDateScalarFunctions {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @ScalarFunction("hive_to_date")
    @Description("hive to_date function")
    @SqlType(StandardTypes.TIMESTAMP)
    public static Slice toDate(@SqlType(StandardTypes.TIMESTAMP) long input) {
        final DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return Slices.utf8Slice(format.format(new Date(input)));
    }
}
