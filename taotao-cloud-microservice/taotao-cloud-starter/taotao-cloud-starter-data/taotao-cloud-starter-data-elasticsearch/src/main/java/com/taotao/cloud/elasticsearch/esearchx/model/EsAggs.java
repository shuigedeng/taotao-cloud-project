package com.taotao.cloud.elasticsearch.esearchx.model;

import java.util.Arrays;
import java.util.function.Consumer;
import org.noear.snack.ONode;

/**
 * es gg
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:05:42
 */
public class EsAggs {

	private final ONode oNode;
	private ONode lastLevl2Node;

	public EsAggs(ONode oNode) {
		this.oNode = oNode;
	}


	private ONode getLevl2Node(String name) {
		lastLevl2Node = oNode.getOrNew(name);
		return lastLevl2Node;
	}

	private void funSet(String asField, String field, String funName) {
		getLevl2Node(asField).getOrNew(funName).set("field", field);
	}

	//
	//============ Metrics =============
	//

	/**
	 * sum，求合
	 */
	public EsAggs sum(String field) {
		funSet(field + "_sum", field, "sum");
		return this;
	}

	/**
	 * avg，求平均值
	 */
	public EsAggs avg(String field) {
		funSet(field + "_avg", field, "avg");
		return this;
	}

	/**
	 * max，求最大值
	 */
	public EsAggs max(String field) {
		funSet(field + "_max", field, "max");
		return this;
	}

	/**
	 * min，求最小值
	 */
	public EsAggs min(String field) {
		funSet(field + "_min", field, "min");
		return this;
	}

	/**
	 * count，值计数
	 */
	public EsAggs count(String field) {
		funSet(field + "_count", field, "value_count");
		return this;
	}

	/**
	 * top_hits，每一个聚合Bucket里面仅返回指定顺序的前N条数据。
	 */
	public EsAggs topHits(int size) {
		return topHits(size, null);
	}

	public EsAggs topHits(int size, Consumer<EsSort> sort) {
		ONode topHits = oNode.getOrNew("$topHits").getOrNew("top_hits");

		topHits.set("size", size);

		if (sort != null) {
			EsSort s = new EsSort(topHits.getOrNew("sort").asArray());
			sort.accept(s);
		}

		return this;
	}

	/**
	 * cardinality，先去重再计数
	 */
	public EsAggs cardinality(String field) {
		funSet(field + "_cardinality", field, "cardinality");
		return this;
	}

	/**
	 * percentiles，多值聚合求百分比
	 */
	public EsAggs percentiles(String field, Number[] percents) {
		ONode oNode1 = getLevl2Node(field + "_percentiles").getOrNew("percentiles");
		oNode1.set("field", field);
		oNode1.getOrNew("percents").addAll(Arrays.asList(percents));
		return this;
	}

	/**
	 * percentiles rank
	 */
	public EsAggs percentilesRank(String field, Number[] values) {
		ONode oNode1 = getLevl2Node(field + "_percentilesRank").getOrNew("percentile_ranks");
		oNode1.set("field", field);
		oNode1.getOrNew("values").addAll(Arrays.asList(values));
		return this;
	}

	/**
	 * extended_stats
	 */
	public EsAggs extendedStats(String field) {
		funSet(field + "_extendedStats", field, "extended_stats");
		return this;
	}

	/**
	 * stats
	 */
	public EsAggs stats(String field) {
		funSet(field + "_stats", field, "stats");
		return this;
	}

	//
	//============ Bucket =============
	//

	/**
	 * filter，聚合
	 */
	public EsAggs filter(Consumer<EsCondition> condition) {
		EsCondition c = new EsCondition(getLevl2Node("$filter").getOrNew("filter"));
		condition.accept(c);

		return this;
	}

	/**
	 * range，聚合
	 */
	public EsAggs range(String field, Consumer<EsRanges> ranges) {
		ONode oNode1 = getLevl2Node(field + "_range").getOrNew("range");

		oNode1.set("field", field);

		EsRanges t = new EsRanges(oNode1.getOrNew("ranges").asArray());
		ranges.accept(t);

		return this;
	}

	/**
	 * terms，聚合
	 */
	public EsAggs terms(String field) {
		terms(field, null);
		return this;
	}

	public EsAggs terms(String field, Consumer<EsTerms> terms) {
		ONode oNode1 = getLevl2Node(field + "_terms").getOrNew("terms");

		if (field.startsWith("$")) {
			oNode1.set("script", field.substring(1));
		} else {
			oNode1.set("field", field);
		}

		if (terms != null) {
			EsTerms t = new EsTerms(oNode1);
			terms.accept(t);
		}

		return this;
	}


	/**
	 * 添加下级条件
	 */
	public EsAggs aggs(Consumer<EsAggs> aggs) {
		if (lastLevl2Node == null) {
			throw new IllegalArgumentException("There are no secondary nodes");
		}

		EsAggs c = new EsAggs(lastLevl2Node.getOrNew("aggs"));
		aggs.accept(c);

		return this;
	}
}
