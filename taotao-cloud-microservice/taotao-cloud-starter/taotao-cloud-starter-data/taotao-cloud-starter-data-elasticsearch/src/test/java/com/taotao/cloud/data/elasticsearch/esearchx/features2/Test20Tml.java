package com.taotao.cloud.data.elasticsearch.esearchx.features2;

import com.taotao.cloud.data.elasticsearch.esearchx.EsContext;
import com.taotao.cloud.data.elasticsearch.esearchx.features.SnowflakeUtils;
import com.taotao.cloud.data.elasticsearch.esearchx.features.model.LogDo;
import com.taotao.cloud.data.elasticsearch.esearchx.model.EsData;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SolonJUnit4ClassRunner.class)
public class Test20Tml {
	final String streamNoExit = "test-demo20_notexit";
	final String templateNoExit = "test-demo20-tml_notexit";
	final String policyNoExit = "test-demo20-policy_notexit";

	final String aliases = "test-demo20";
	final String template = "test-demo20-tml";
	final String policy = "test-demo20-policy";


	@Inject("${test.esx}")
	EsContext context;

	@Test
	public void test1() throws Exception {
		assert context.templateExist(templateNoExit) == false;
		assert context.policyExist(policyNoExit) == false;
		assert context.templateExist(template);
	}

	@Test
	public void test2() throws Exception {
		//创建或者更新微略
		String policy_dsl = Utils.getResourceAsString("demo20/log-policy_dsl.json");

		String policy_dsl_rst = context.policyCreate(policy, policy_dsl);
		System.out.println(policy_dsl_rst);


		//创建或者更新模板
		String tml_dsl = Utils.getResourceAsString("demo20/log-index.json");

		ONode tmlDslNode = ONode.loadStr(tml_dsl);
		//设定匹配模式
		tmlDslNode.getOrNew("index_patterns").val(aliases + "-*");
		//设定别名
		tmlDslNode.get("template").getOrNew("aliases").getOrNew(aliases).asObject(); //stream 不需要别名
		//设定策略
		tmlDslNode.get("template").get("settings").get("index.lifecycle.name").val(policy);
		//设定翻转别名
		tmlDslNode.get("template").get("settings").get("index.lifecycle.rollover_alias").val(aliases);

		String index_dsl_rst = context.templateCreate(template, tmlDslNode.toJson());
		System.out.println(index_dsl_rst);

	}


	Random random = new Random();

	@Test
	public void test_add() throws Exception {
		String json = Utils.getResourceAsString("demo/log.json", "utf-8");


		LogDo logDo = new LogDo();
		logDo.logger = "waterapi";
		logDo.log_id = SnowflakeUtils.genId();
		logDo.trace_id = Utils.guid();
		logDo.class_name = this.getClass().getName();
		logDo.thread_name = Thread.currentThread().getName();
		logDo.tag = "map1";
		logDo.level = (random.nextInt() % 5) + 1;
		logDo.content = json;
		logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
		logDo.log_fulltime = new Date();

		ONode doc = ONode.loadObj(logDo).build(n -> {
			n.set("@timestamp", logDo.log_fulltime);
		});

		String indiceName = aliases + "-in";

		String rst = context.indice(indiceName).insert(doc);
		System.out.println(rst);
	}

	@Test
	public void test_addlist() throws Exception {
		String json = Utils.getResourceAsString("demo/log.json", "utf-8");

		List<ONode> docs = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			LogDo logDo = new LogDo();
			logDo.logger = "waterapi";
			logDo.log_id = SnowflakeUtils.genId();
			logDo.trace_id = Utils.guid();
			logDo.class_name = this.getClass().getName();
			logDo.thread_name = Thread.currentThread().getName();
			logDo.tag = "map1";
			logDo.level = (random.nextInt() % 5) + 1;
			logDo.content = json;
			logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
			logDo.log_fulltime = new Date();

			docs.add(ONode.loadObj(logDo).build(n -> {
				n.set("@timestamp", logDo.log_fulltime);
			}));
		}


		String indiceName = aliases + "-in";

		String rst = context.indice(indiceName).insertList(docs);
		System.out.println(rst);
		assert rst.contains("\"errors\":false");
	}

	@Test
	public void test_terms() throws Exception {
		EsData<LogDo> result = context.indice(aliases)
			.where(c -> c.terms("tag", "list1", "map1"))
			.limit(0, 10)
			.selectList(LogDo.class);

		int rst = result.getListSize();
		System.out.println(rst);
		assert rst >= 10;
	}

	@Test
	public void test_prefix() throws Exception {

		EsData<LogDo> result = context.indice(aliases)
			.where(c -> c.prefix("tag", "m"))
			.limit(0, 10)
			.selectList(LogDo.class);

		assert result.getListSize() >= 10;
		assert result.getList().get(0).log_id > 0;
	}
}
