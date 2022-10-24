package com.taotao.cloud.data.elasticsearch.esearchx.features;

import com.taotao.cloud.data.elasticsearch.esearchx.EsContext;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

import java.io.IOException;

/**
 * ElasticSearch 测试
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test0 {
    final String indiceNoExit = "test-user_logx";
    final String indiceNew = "test-user_log_new";
    final String indice = "test-user_log_202110";
    final String alias = "test-user_log";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化


    @Test
    public void test0() throws Exception {
        assert context.indiceExist(indiceNoExit) == false;
    }

    @Test
    public void test1() throws Exception {
        if (context.indiceExist(indiceNew)) {
            context.indiceDrop(indiceNew);
        }

        assert context.indiceExist(indiceNew) == false;

        String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
        context.indiceCreate(indiceNew, dsl);


        assert context.indiceExist(indiceNew) == true;
    }

    @Test
    public void test1Drop() throws Exception {
        context.indiceDrop(indiceNew);
    }

    @Test
    public void test1Exist() throws Exception {
        context.indiceExist(indiceNew);
    }


    private void test2Create(String name) throws IOException {
        if (context.indiceExist(name) == false) {
            String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
            context.indiceCreate(name, dsl);
        }

        assert context.indiceExist(name) == true;
    }

    @Test
    public void test2() throws Exception {
        test2Create(indice);

        context.indiceSettings(indice, s -> s.setRefreshInterval("5s"));
    }

    @Test
    public void test3() throws Exception {
        test2Create("test-user_log_202110");
        test2Create("test-user_log_202109");
        test2Create("test-user_log_202108");
        test2Create("test-user_log_202107");
        test2Create("test-user_log_202106");
        test2Create("test-user_log_202105");
    }

    @Test
    public void test4() throws Exception {
        context.indiceAliases(a -> a
                .add("test-user_log_202110", alias)
                .add("test-user_log_202109", alias)
                .add("test-user_log_202108", alias)
                .add("test-user_log_202107", alias)
                .add("test-user_log_202106", alias)
                .add("test-user_log_202105", alias));
    }

    @Test
    public void test5() throws Exception {
        String json = context.indiceShow(indice);
        System.out.println(json);
        ONode oNode = ONode.loadStr(json);

        assert oNode.get(indice).contains("mappings");
        assert oNode.get(indice).contains("settings");

        ////
        try {
            context.indiceShow(indiceNoExit); //不存在会 404 错误
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
}
