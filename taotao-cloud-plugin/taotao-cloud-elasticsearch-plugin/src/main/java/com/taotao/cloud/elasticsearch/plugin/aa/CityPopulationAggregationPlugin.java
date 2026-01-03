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

package com.taotao.cloud.elasticsearch.plugin.aa;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.plugins.SearchPlugin.AggregationSpec;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;

/**
 * CityPopulationAggregationPlugin
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class CityPopulationAggregationPlugin extends Plugin {

    public CityPopulationAggregationPlugin( Settings settings ) {
        // do nothing
    }

    @Override
    public Map<String, AggregationSpec> getAggregations() {
        Map<String, AggregationSpec> aggregations = new HashMap<>();
        aggregations.put("city_population", new CityPopulationAggregationSpec());
        return aggregations;
    }

    private static class CityPopulationAggregationSpec implements AggregationSpec {

        @Override
        public InternalAggregation reduce( ReduceContext reduceContext ) {
            Map<String, Long> cityCounts = new HashMap<>();
            for (ShardReduceContext shardReduceContext : reduceContext) {
                for (String city : shardReduceContext.data().keySet()) {
                    long count = cityCounts.getOrDefault(city, 0L);
                    count += shardReduceContext.data().get(city).longValue();
                    cityCounts.put(city, count);
                }
            }
            List<Bucket> buckets =
                    cityCounts.entrySet().stream()
                            .map(
                                    entry ->
                                            new InternalTerms.Bucket(
                                                    entry.getKey(),
                                                    entry.getValue(),
                                                    InternalAggregation.Reduce.EMPTY))
                            .collect(Collectors.toList());
            return new InternalTerms(
                    "city_population", 0, InternalAggregation.Reduce.EMPTY, null, null, buckets, 0);
        }

        @Override
        public boolean needsScores() {
            return false;
        }
    }
}
