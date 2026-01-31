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

package com.taotao.cloud.seatunnel;

import com.google.auto.service.AutoService;

import java.util.List;

import org.apache.seatunnel.api.table.type.BasicType;
import org.apache.seatunnel.api.table.type.SeaTunnelDataType;
import org.apache.seatunnel.transform.sql.zeta.ZetaUDF;

/**
 * ExampleUDF
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@AutoService(ZetaUDF.class)
public class ExampleUDF implements ZetaUDF {

    @Override
    public String functionName() {
        return "EXAMPLE";
    }

    @Override
    public SeaTunnelDataType<?> resultType( List<SeaTunnelDataType<?>> argsType ) {
        return BasicType.STRING_TYPE;
    }

    @Override
    public Object evaluate( List<Object> args ) {
        String arg = (String) args.get(0);
        if (arg == null)
            return null;
        return "UDF: " + arg;
    }
}
