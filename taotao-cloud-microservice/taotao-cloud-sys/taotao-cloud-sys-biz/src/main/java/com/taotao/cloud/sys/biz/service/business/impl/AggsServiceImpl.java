package com.taotao.cloud.sys.biz.service.business.impl;


import com.taotao.cloud.sys.biz.service.business.IAggsService;
import org.springframework.stereotype.Service;


/**
 * AggsServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-03 14:46:53
 */
@Service
public class AggsServiceImpl implements IAggsService {

	//@Autowired(required = false)
	//RestHighLevelClient client;
	//
	//@Override
	//public ResultData termsAggs(QueryCommand content) throws Exception {
	//    SearchRequest searchRequest = new SearchRequest(content.getIndexname());
	//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//    TermsAggregationBuilder aggregation = AggregationBuilders.terms("countnumber").field(content.getAggsField()).size(10)
	//            .order(BucketOrder.key(true));
	//    searchSourceBuilder.trackTotalHits(true);
	//    searchSourceBuilder.query(queryBuilder).aggregation(aggregation);
	//    searchRequest.source(searchSourceBuilder);
	//    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//    Aggregations result = searchResponse.getAggregations();
	//    Terms byCompanyAggregation = result.get("countnumber");
	//    List<? extends Terms.Bucket> bucketList = byCompanyAggregation.getBuckets();
	//    List<BucketResult> list = new ArrayList<>();
	//    for (Terms.Bucket bucket : bucketList) {
	//        BucketResult br = new BucketResult(bucket.getKeyAsString(), bucket.getDocCount());
	//        list.add(br);
	//    }
	//    ResultData resultData = new ResultData();
	//    resultData.setQtime(new Date());
	//    resultData.setData(list);
	//    resultData.setNumberFound(searchResponse.getHits().getTotalHits().value);
	//    resultData.setStart(content.getStart());
	//    return resultData;
	//}
	//
	//@Override
	//public ResultData rangeAggs(RangeQuery content) throws Exception {
	//    SearchRequest searchRequest = new SearchRequest(content.getIndexname());
	//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//    searchSourceBuilder.trackTotalHits(true);
	//    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//    // 聚集
	//    String dateField = content.getAggsField();
	//    if (content.getRangeValues() != null && content.getRangeValues().size() > 0) {
	//        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders
	//                .dateRange("aggsName")
	//                .field(dateField);
	//        for (int i = 0; i < content.getRangeValues().size(); i++) {
	//            String from =content.getRangeValues().get(i).getFrom();
	//            String to = content.getRangeValues().get(i).getTo();
	//            if(StringUtils.isNoneBlank(from)&&StringUtils.isBlank(to)){
	//                dateRangeAggregationBuilder.addUnboundedFrom(from);
	//            }else if(StringUtils.isNoneBlank(to) &&StringUtils.isBlank(from)){
	//                dateRangeAggregationBuilder.addUnboundedTo(to);
	//            }else if(StringUtils.isNoneBlank(from)&&StringUtils.isNoneBlank(to)){
	//                dateRangeAggregationBuilder.addRange(from, to);
	//            }
	//        }
	//        searchSourceBuilder.query(queryBuilder).aggregation(dateRangeAggregationBuilder);
	//    } else {
	//        searchSourceBuilder.query(queryBuilder);
	//    }
	//    searchRequest.source(searchSourceBuilder);
	//    searchSourceBuilder.from(content.getStart());
	//    searchSourceBuilder.size(content.getRows());
	//    SearchResponse searchResponse = null;
	//
	//    searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//    Aggregations jsonAggs = searchResponse.getAggregations();
	//    Range range = (Range) jsonAggs.get("aggsName");
	//    List<? extends Range.Bucket> bucketList = range.getBuckets();
	//    List<BucketResult> list = new ArrayList<>();
	//    for (Range.Bucket bucket : bucketList) {
	//    	BucketResult br = new BucketResult(bucket.getKeyAsString(), bucket.getDocCount());
	//        list.add(br);
	//    }
	//    ResultData resultData = new ResultData();
	//    resultData.setQtime(new Date());
	//    resultData.setData(list);
	//    resultData.setNumberFound(searchResponse.getHits().getTotalHits().value);
	//    resultData.setStart(content.getStart());
	//    return resultData;
	//}
	//
	//@Override
	//public ResultData histogramAggs(QueryCommand content) throws Exception {
	//    SearchRequest searchRequest = new SearchRequest(content.getIndexname());
	//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//    searchSourceBuilder.trackTotalHits(true);
	//    // 聚集
	//    String dateField = content.getAggsField();
	//    Integer step = content.getStep();
	//    if (step != null && dateField != null) {
	//        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders
	//                .histogram("aggsName")
	//                .field(dateField)
	//                .interval(content.getStep())
	//                .minDocCount(0L);
	//        searchSourceBuilder.query(queryBuilder).aggregation(histogramAggregationBuilder);
	//    } else {
	//        searchSourceBuilder.query(queryBuilder);
	//    }
	//    searchRequest.source(searchSourceBuilder);
	//    searchSourceBuilder.from(content.getStart());
	//    searchSourceBuilder.size(content.getRows());
	//    SearchResponse searchResponse = null;
	//
	//    List<HashMap<String, Long>> list = new ArrayList<>();
	//    searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//    Aggregations jsonAggs = searchResponse.getAggregations();
	//    Histogram dateHistogram = (Histogram) jsonAggs.get("aggsName");
	//    List<? extends Histogram.Bucket> bucketList = dateHistogram.getBuckets();
	//    for (Histogram.Bucket bucket : bucketList) {
	//        HashMap<String, Long> resultMap = new HashMap<String, Long>();
	//        resultMap.put(bucket.getKeyAsString(), bucket.getDocCount());
	//        list.add(resultMap);
	//    }
	//    ResultData resultData = new ResultData();
	//    resultData.setQtime(new Date());
	//    resultData.setData(list);
	//    resultData.setNumberFound(searchResponse.getHits().getTotalHits().value);
	//    resultData.setStart(content.getStart());
	//    return resultData;
	//}
	//
	//@Override
	//public ResultData datehistogramAggs(QueryCommand content) throws Exception {
	//    SearchRequest searchRequest = new SearchRequest(content.getIndexname());
	//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//    searchSourceBuilder.trackTotalHits(true);
	//    // 聚集
	//    String dateField = content.getAggsField();
	//    Integer step = content.getStep();
	//    if (step != null && dateField != null) {
	//        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders
	//                .dateHistogram("aggsName")
	//                .field(dateField)
	//                .fixedInterval(DateHistogramInterval.seconds(step))
	//                // .extendedBounds(new ExtendedBounds("2020-09-01 00:00:00", "2020-09-02 05:00:00")
	//                .minDocCount(0L);
	//        searchSourceBuilder.query(queryBuilder).aggregation(dateHistogramAggregationBuilder);
	//    } else {
	//        searchSourceBuilder.query(queryBuilder);
	//    }
	//    searchRequest.source(searchSourceBuilder);
	//    searchSourceBuilder.from(content.getStart());
	//    searchSourceBuilder.size(content.getRows());
	//    SearchResponse searchResponse = null;
	//    searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//    Aggregations jsonAggs = searchResponse.getAggregations();
	//    Histogram dateHistogram = (Histogram) jsonAggs.get("aggsName");
	//    List<? extends Histogram.Bucket> bucketList = dateHistogram.getBuckets();
	//    List<BucketResult> list = new ArrayList<>();
	//    for (Histogram.Bucket bucket : bucketList) {
	//        BucketResult br = new BucketResult(bucket.getKeyAsString(), bucket.getDocCount());
	//        list.add(br);
	//    }
	//    ResultData resultData = new ResultData();
	//    resultData.setQtime(new Date());
	//    resultData.setData(list);
	//    resultData.setNumberFound(searchResponse.getHits().getTotalHits().value);
	//    resultData.setStart(content.getStart());
	//    return resultData;
	//}
	//
	//@Override
	//public ResultData nestedTermsAggs() throws Exception {
	//	SearchRequest searchRequest = new SearchRequest("city");
	//    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	//    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
	//    NestedAggregationBuilder aggregation = AggregationBuilders.nested("nestedAggs", "country")
	//    		.subAggregation(AggregationBuilders.terms("groupbycountry")
	//    						.field("country.countryname.keyword").size(100)
	//    						.order(BucketOrder.count(false)));
	//    searchSourceBuilder.query(queryBuilder).aggregation(aggregation);
	//    searchSourceBuilder.trackTotalHits(true);
	//    searchRequest.source(searchSourceBuilder);
	//    SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	//    Nested result = searchResponse.getAggregations().get("nestedAggs");
	//    Terms groupbycountry = result.getAggregations().get("groupbycountry");
	//    List<? extends Terms.Bucket> bucketList = groupbycountry.getBuckets();
	//    List<BucketResult> list = new ArrayList<>();
	//    for (Terms.Bucket bucket : bucketList) {
	//        BucketResult br = new BucketResult(bucket.getKeyAsString(), bucket.getDocCount());
	//        list.add(br);
	//    }
	//    ResultData resultData = new ResultData();
	//    resultData.setQtime(new Date());
	//    resultData.setData(list.subList(0, 10));
	//    resultData.setNumberFound(searchResponse.getHits().getTotalHits().value);
	//    resultData.setStart(0);
	//    return resultData;
	//}


}
