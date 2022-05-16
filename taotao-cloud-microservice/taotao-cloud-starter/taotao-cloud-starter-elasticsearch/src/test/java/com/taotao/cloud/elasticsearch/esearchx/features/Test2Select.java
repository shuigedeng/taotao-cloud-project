package com.taotao.cloud.elasticsearch.esearchx.features;

import com.taotao.cloud.elasticsearch.esearchx.EsContext;
import com.taotao.cloud.elasticsearch.esearchx.EsGlobal;
import com.taotao.cloud.elasticsearch.esearchx.features.model.LogDo;
import com.taotao.cloud.elasticsearch.esearchx.model.EsData;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class Test2Select {

    final String indice = "test-user_log";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化

    static {
        EsGlobal.onCommandBefore(cmd -> System.out.println("dsl:::" + cmd.getDsl()));
    }

    @Test
    public void test_selectById() throws Exception {


        LogDo logDo = context.indice("test-user_log_202110").selectById(LogDo.class, "1");
        assert logDo != null;
        assert logDo.log_id == 1;


        logDo = context.indice("test-user_log_202106").selectById(LogDo.class, "1");
        assert logDo == null;
    }

    @Test
    public void test_selectByIds() throws Exception {
        List<LogDo> result = context.indice(indice)
                .selectByIds(LogDo.class, Arrays.asList("1", "2"));

        System.out.println(result);

        assert result != null;
        assert result.size() == 2;
    }

    @Test
    public void test_term() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_term_score() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.useScore().term("tag", "list1"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_terms() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.terms("tag", "list1", "map1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_prefix() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.prefix("tag", "m"))
                .limit(0, 10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }


    @Test
    public void test_match() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_match_h() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("tag", "list1"))
                .highlight(h->h.addField("tag", f->f.preTags("<em>").postTags("</em>").requireMatch(true)))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_match2() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.match("content", "class_name"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrase() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrase("tag", "list1"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhraseSolp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrase("tag", "list1", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_matchPhrasePrefix() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "class"))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_matchPhrasePrefixSolp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "name tag", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 0;


        result = context.indice(indice)
                .where(c -> c.matchPhrasePrefix("content", "key", 2))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_range() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.range("level", r -> r.gt(3)))
                .limit(0, 10)
                .selectList(LogDo.class);

        System.out.println(result);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test_wildcard() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.wildcard("tag", "l*"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_regexp() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.regexp("tag", "l.*?"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_exists() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.exists("tag"))
                .limit(10)
                .selectList(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test_script() throws Exception {

        EsData<LogDo> result = context.indice(indice)
                .where(c -> c.script("doc['tag'].value.length() >= params.len", p -> p.set("len", 2)))
                .limit(10)
                .selectList(LogDo.class);

        System.out.println(result);
        assert result.getListSize() == 10;
    }


    @Test
    public void test_selectMapList() throws Exception {
        List<Map> result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(10)
                .selectMapList();

        System.out.println(result);

        assert result.size() == 10;
    }


    @Test
    public void test_selectMap() throws Exception {
        Map result = context.indice(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(1)
                .selectMap();

        assert result != null;

        System.out.println(result);

        assert result.size() >= 10;
    }

    @Test
    public void test_selectMap_h() throws Exception {
        Map result = context.indice(indice)
                .where(c -> c.match("content", "tag"))
                .highlight(h -> h.addField("content", f -> f.preTags("<em>").postTags("</em>")))
                .timeout(10)
                .limit(1)
                .selectMap();

        assert result != null;

        System.out.println(result);

        assert result.size() >= 10;
    }

    @Test
    public void test_selectSql() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(indice).append(" limit 10");

        List<Map> result = context.indice(indice)
                .sql(sql.toString())
                .selectMapList();

        assert result != null;

        System.out.println(result);

        assert result.size() >= 2;
        assert result.get(0).size() >= 2;
    }
}
