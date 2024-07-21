package com.taotao.cloud.elasticsearch.plugin.aa;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin.AggregationSpec;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CityPopulationAggregationPlugin extends Plugin {

	public CityPopulationAggregationPlugin(Settings settings) {
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
		public InternalAggregation reduce(ReduceContext reduceContext) {
			Map<String, Long> cityCounts = new HashMap<>();
			for (ShardReduceContext shardReduceContext : reduceContext) {
				for (String city : shardReduceContext.data().keySet()) {
					long count = cityCounts.getOrDefault(city, 0L);
					count += shardReduceContext.data().get(city).longValue();
					cityCounts.put(city, count);
				}
			}
			List<Bucket> buckets = cityCounts.entrySet().stream()
				.map(entry -> new InternalTerms.Bucket(entry.getKey(), entry.getValue(),
					InternalAggregation.Reduce.EMPTY))
				.collect(Collectors.toList());
			return new InternalTerms("city_population", 0, InternalAggregation.Reduce.EMPTY, null,
				null, buckets, 0);
		}

		@Override
		public boolean needsScores() {
			return false;
		}
	}
}
