package com.taotao.cloud.sys.biz.controller.tools.elasticsearch.elastic.service;


import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.QueryCommand;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.RangeQuery;
import com.taotao.cloud.sys.biz.controller.tools.elasticsearch.pagemodel.ResultData;

public interface AggsService {
	// 词条聚集
	ResultData termsAggs(QueryCommand content) throws Exception;
	// 范围聚集
	ResultData rangeAggs(RangeQuery content) throws Exception;
	// 直方图聚集
	ResultData histogramAggs(QueryCommand content) throws Exception;
	// 日期直方图聚集
	ResultData datehistogramAggs(QueryCommand content) throws Exception;
	// 嵌套对象词条聚集
	ResultData nestedTermsAggs() throws Exception;
}
