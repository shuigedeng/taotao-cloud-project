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

package com.taotao.cloud.trino;

import com.google.common.collect.ImmutableSet;
import com.taotao.cloud.data.analysis.trino.udaf.avg.AvgAggregationFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.collect_list.CollectListAggregationFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.decode_bit_set.DecodeBitSetAggregationFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.decode_bit_set.RouteUserGroupAggregationFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.funnel.FunnelAggregationsFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.funnel.FunnelMergeAggregationsFunctions;
import com.taotao.cloud.data.analysis.trino.udaf.sum_double.SumDoubleAggregationsFunctions;
import com.taotao.cloud.data.analysis.trino.udf.hive_to_date.HiveToDateScalarFunctions;
import com.taotao.cloud.data.analysis.trino.udf.str_upper.StrUpperScalarFunctions;
import io.trino.spi.Plugin;
import java.util.Set;

/**
 * 开发完成后打成jar包上传到trino服务器 mkdir ${TRINO_HOME}/plugin/udf
 * <p>
 * cp taotao-cloud-trino-all.jar ${TRINO_HOME}/plugin/udf
 * <p>
 * ${TRINO_HOME}/bin/launcher restart
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:37
 */
public class TrinoFunctionsPlugin implements Plugin {

    @Override
    public Set<Class<?>> getFunctions() {
        return ImmutableSet.<Class<?>>builder()
                // udaf
                .add(AvgAggregationFunctions.class)
                .add(CollectListAggregationFunctions.class)
                .add(DecodeBitSetAggregationFunctions.class)
                .add(RouteUserGroupAggregationFunctions.class)
                .add(FunnelAggregationsFunctions.class)
                .add(FunnelMergeAggregationsFunctions.class)
                .add(SumDoubleAggregationsFunctions.class)
                // udf
                .add(HiveToDateScalarFunctions.class)
                .add(StrUpperScalarFunctions.class)
                .build();
    }
}
