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
import org.elasticsearch.index.IndexModule;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin.AggregationSpec;

public class MyPlugin extends Plugin {

    @Override
    public void onModule(AnalysisModule analysisModule) {
        analysisModule.addTokenizer("lowercase", LowercaseTokenizerFactory::new);
    }

    @Override
    public void onIndexModule(IndexModule indexModule) {
        indexModule.addIndexStore("file", FileIndexStorePlugin::new);
    }

    @Override
    public Map<String, AggregationSpec> getAggregations() {
        Map<String, AggregationSpec> aggregations = new HashMap<>();
        aggregations.put(
                "city_population",
                new CityPopulationAggregationPlugin.CityPopulationAggregationSpec());
        return aggregations;
    }
}
