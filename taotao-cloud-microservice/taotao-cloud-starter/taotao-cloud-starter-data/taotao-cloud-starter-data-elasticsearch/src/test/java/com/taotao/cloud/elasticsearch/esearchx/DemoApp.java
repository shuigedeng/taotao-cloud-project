/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.elasticsearch.esearchx;

import com.taotao.cloud.elasticsearch.esearchx.EsContext;
import com.taotao.cloud.elasticsearch.esearchx.EsGlobal;
import com.taotao.cloud.elasticsearch.esearchx.features.model.LogDo;
import com.taotao.cloud.elasticsearch.esearchx.model.EsData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.noear.snack.ONode;
import org.noear.snack.core.Options;

/**
 * DemoApp
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:28
 */
public class DemoApp {

	String tableCreateDsl = "...";

	public void demo() throws IOException {
		//执行前打印dsl
		EsGlobal.onCommandBefore(cmd -> System.out.println("dsl:::" + cmd.getDsl()));

		//实例化上下文
		EsContext esx = new EsContext("localhost:30480");

		//创建索引
		esx.indiceCreate("user_log_20200101", tableCreateDsl);
		esx.indiceCreate("user_log_20200102", tableCreateDsl);
		esx.indiceCreate("user_log_20200103", tableCreateDsl);

		//构建索引别名
		esx.indiceAliases(a -> a
			.add("user_log_20200101", "user_log")
			.add("user_log_20200102", "user_log")
			.add("user_log_20200103", "user_log"));

		//删除索引（如果存在就删了；当然也可以直接删）
		if (esx.indiceExist("user_log_20200101")) {
			esx.indiceDrop("user_log_20200101");
		}

		//单条插入
		LogDo logDo = new LogDo();
		esx.indice("user_log").insert(logDo);

		//单条插入::增加序列化选项定制
		Options option = Options.def();
		esx.indice("user_log").options(option).insert(logDo);
		esx.indice("user_log").options(options -> {
			//增加类型编码
			options.addEncoder(Long.class,
				(data, node) -> node.val().setString(String.valueOf(data)));
		}).insert(logDo);

		//批量插入
		List<LogDo> list = new ArrayList<>();
		list.add(new LogDo());
		esx.indice("user_log").insertList(list);

		//单条插入或更新
		esx.indice("user_log").upsert("1", logDo);

		//批量插入或更新
		Map<String, LogDo> add = new LinkedHashMap<>();
		add.put("...", new LogDo());
		esx.indice("user_log").upsertList(add);

		//一个简单的查询
		LogDo result = esx.indice("user_log").selectById(LogDo.class, "1");

		//一个带条件的查询
		EsData<LogDo> result1 = esx.indice("user_log")
			.where(r -> r.term("level", 5))
			.orderByDesc("log_id")
			.limit(50)
			.selectList(LogDo.class);

		//一个结果高亮的查询
		Map result2 = esx.indice("indice")
			.where(c -> c.match("content", "tag"))
			.highlight(h -> h.addField("content", f -> f.preTags("<em>").postTags("</em>")))
			.limit(1)
			.selectMap();

		//一个复杂些的查询
		EsData<LogDo> result3 = esx.indice("indice")
			.where(c -> c.useScore().must()
				.term("tag", "list1")
				.range("level", r -> r.gt(3)))
			.orderByAsc("level")
			.andByAsc("log_id")
			.minScore(1)
			.limit(50, 50)
			.selectList(LogDo.class);

		//脚本查询
		EsData<LogDo> result4 = esx.indice("indice")
			.where(c -> c.script("doc['tag'].value.length() >= params.len", p -> p.set("len", 2)))
			.limit(10)
			.selectList(LogDo.class);

		//聚合查询
		ONode result5 = esx.indice("indice")
			.where(w -> w.term("tag", "list1"))
			.limit(0)
			.aggs(a -> a.terms("level", t -> t.size(20))
				.aggs(a1 -> a1.topHits(2, s -> s.addByAes("log_fulltime"))))
			.selectAggs();
	}
}
