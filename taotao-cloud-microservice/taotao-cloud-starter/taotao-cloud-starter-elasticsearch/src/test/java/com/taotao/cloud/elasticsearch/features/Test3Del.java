package com.taotao.cloud.elasticsearch.features;


import com.taotao.cloud.elasticsearch.esearchx.EsContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * ElasticSearch 测试
 *
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class Test3Del {

    final String indice = "test-user_log_202110";


    @Inject("${test.esx}")
    EsContext context;
    //EsContext context = new EsContext("eshost:30480"); //直接实例化

    @Test
    public void test0() throws Exception {
        if (context.indice(indice).deleteById("5")) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    public void test1() throws Exception {
        String tmp = context.indice(indice).where(q->q.term("level", 1)).delete();

        System.out.println(tmp);
    }
}
