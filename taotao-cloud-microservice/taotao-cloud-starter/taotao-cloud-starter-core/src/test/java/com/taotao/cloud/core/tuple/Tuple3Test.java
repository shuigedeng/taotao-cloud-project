package com.taotao.cloud.core.tuple;

import com.taotao.cloud.common.support.tuple.tuple.Tuple3;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple3Test {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWith() {
        Tuple3<Integer, String, Double> tuple3 = Tuple3.with(123, "test", 186.5);
        log.debug("tuple3:{}", tuple3.toString());
        log.debug("first:{}", tuple3.first);
        log.debug("second:{}", tuple3.second);
        log.debug("third:{}", tuple3.third);
    }

    @Test
    public void testSwap() {
        Tuple3<Integer, String, Double> tuple3 = Tuple3.with(123, "test", 186.5);
        log.debug("reverse:{}", tuple3.reverse().toString());
    }
}
