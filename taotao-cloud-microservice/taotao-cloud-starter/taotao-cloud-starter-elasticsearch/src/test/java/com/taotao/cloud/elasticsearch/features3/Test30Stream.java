package com.taotao.cloud.elasticsearch.features3;

import com.taotao.cloud.elasticsearch.esearchx.EsContext;
import com.taotao.cloud.elasticsearch.esearchx.model.EsData;
import com.taotao.cloud.elasticsearch.features.SnowflakeUtils;
import com.taotao.cloud.elasticsearch.features.model.LogDo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

@RunWith(SolonJUnit4ClassRunner.class)
public class Test30Stream {
    final String streamNoExit = "test-demo30_notexit";
    final String templateNoExit = "test-demo30-tml_notexit";
    final String policyNoExit = "test-demo30-policy_notexit";

    final String aliases = "test-demo30";
    final String template = "test-demo30-tml";
    final String policy = "test-demo30-policy";


    @Inject("${test.esx}")
    EsContext context;

    @Test
    public void test1() throws Exception {
        assert context.templateExist(templateNoExit) == false;
        assert context.policyExist(policyNoExit) == false;
        assert context.templateExist(template);
    }

    @Test
    public void test1_2() throws Exception{
        String policyName = "water.water_log_faas.stream-policy";

        assert context.policyExist(policyName);

        String policy_dsl_show = context.policyShow(policyName);
        //不要用select，避免policy带"."
        ONode policyDslNode = new ONode().set("policy", ONode.load(policy_dsl_show).get(policyName).get("policy"));
        ONode minAgeNode = policyDslNode.select("policy.phases.delete.min_age");

        System.out.println(minAgeNode.getString());

        assert minAgeNode.getString().equals("15d");
    }

    @Test
    public void test2() throws Exception {
        //创建或者更新微略
        String policy_dsl = Utils.getResourceAsString("demo30/log-policy_dsl.json");

        String policy_dsl_rst = context.policyCreate(policy, policy_dsl);
        System.out.println(policy_dsl_rst);

        String policy_dsl_show = context.policyShow(policy);
        System.out.println(policy_dsl_show);
        //不要用select，避免policy带"."
        ONode policy_dsl_show_tml = new ONode().set("policy", ONode.load(policy_dsl_show).get(policy).get("policy"));
        System.out.println(policy_dsl_show_tml.toJson());
        context.policyCreate(policy, policy_dsl_show_tml.toJson());

        //创建或者更新模板
        String tml_dsl = Utils.getResourceAsString("demo30/log-index.json");

        ONode tmlDslNode = ONode.loadStr(tml_dsl);
        //设定匹配模式
        tmlDslNode.getOrNew("index_patterns").val(aliases + "-*");
        //设定策略
        tmlDslNode.get("template").get("settings").get("index.lifecycle.name").val(policy);
        //设定翻转别名
        tmlDslNode.get("template").get("settings").get("index.lifecycle.rollover_alias").val(aliases);

        String tml_dsl_rst = context.templateCreate(template, tmlDslNode.toJson());
        System.out.println(tml_dsl_rst);


        //获取模板
        String tml_dsl_show = context.templateShow(template);
        System.out.println(tml_dsl_show);

        //再次修改
        ONode tml_dsl_show_tml = ONode.load(tml_dsl_show).select("index_templates[0].index_template");
        System.out.println(tml_dsl_show_tml.toJson());
        tml_dsl_rst = context.templateCreate(template, tml_dsl_show_tml.toJson());
        System.out.println(tml_dsl_rst);

//        String stream_rst = context.streamCreate(stream);
//        System.out.println(stream_rst);
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


        String rst = context.stream(aliases + "-in").insert(doc);
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

        String rst = context.stream(aliases + "-in").insertList(docs);
        System.out.println(rst);
        assert rst.contains("\"errors\":false");
    }

    @Test
    public void test_terms() throws Exception {
        EsData<LogDo> result = context.indice(aliases + "-in")
                .where(c -> c.terms("tag", "list1", "map1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        int rst = result.getListSize();
        System.out.println(rst);
        assert rst >= 10;
    }

    @Test
    public void test_prefix() throws Exception {

        EsData<LogDo> result = context.indice(aliases + "-in")
                .where(c -> c.prefix("tag", "m"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() >= 10;
        assert result.getList().get(0).log_id > 0;
    }
}
