package com.taotao.cloud.elasticsearch.esearchx.features;

import com.taotao.cloud.elasticsearch.esearchx.EsContext;
import com.taotao.cloud.elasticsearch.esearchx.features.model.LogDo;
import com.taotao.cloud.elasticsearch.esearchx.model.EsData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class Test2SelectPlus {

    final String indice = "test-user_log";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化


    @Test
    public void test1() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.must().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test1_filter() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.filter().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test1_score() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().must().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test2() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test3() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.must().match("tag", "list1").term("level", 3))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test4() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.must()
                        .match("tag", "list1")
                        .term("level", 3)
                        .add(c1 -> c1.mustNot()
                                .matchPhrasePrefix("summary", "${")
                                .matchPhrasePrefix("summary", "#{")))
                .limit(0, 10)
                .orderBy(s -> s.addByDesc("log_id"))
                //.orderByDesc("log_id")
                .selectList(LogDo.class);

        System.out.println(result);
        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test5() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class, "log_id,trace_id");

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
        assert result.getList().get(0).tag == null;
    }

    @Test
    public void test6() throws Exception {
        //输出字段控制（排除模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class, "!log_id,trace_id");

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id == 0;
        assert result.getList().get(0).tag != null;
    }

    @Test
    public void test7() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(5)
                .orderBy(s -> s.addByAes("log_id"))
                //.orderByAsc("log_id")
                .onAfter(239467464128819200l)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 5;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test8() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderBy(s -> s.addByDesc("log_id"))
                //.orderByDesc("log_id")
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > result.getList().get(1).log_id;
    }

    @Test
    public void test9() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderBy(s -> s.addByDesc("level").addByAes("log_id"))
//                .orderByDesc("level")
//                .andByAsc("log_id")
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test10() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().must()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .limit(0, 10)
//                .orderBy(s -> s.andAes("level").andAes("log_id"))
                .orderByAsc("level")
                .andByAsc("log_id")
                //.minScore(1)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).level > 3;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test11() throws Exception {
        //输出字段控制（选择模式）
        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.filter()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .limit(0, 10)
//                .orderBy(s -> s.andAes("level").andAes("log_id"))
                .orderByAsc("level")
                .andByAsc("log_id")
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).level > 3;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }
}
