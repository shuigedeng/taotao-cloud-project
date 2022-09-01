package com.taotao.cloud.core.tuple;

import com.taotao.cloud.common.support.tuple.tuple.Tuple4;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple4Test {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWith() {
        Tuple4 tuple4 = Tuple4.with(123, "test", 186.5, true);
        log.debug("tuple4:{}", tuple4.toString());
        log.debug("first:{}", tuple4.first);
        log.debug("second:{}", tuple4.second);
        log.debug("third:{}", tuple4.third);
        log.debug("fourth:{}", tuple4.fourth);
    }

    @Test
    public void testSwap() {
        Tuple4 tuple4 = Tuple4.with(123, "test", 186.5, true);
        log.debug("reverse:{}", tuple4.reverse().toString());
    }
}
