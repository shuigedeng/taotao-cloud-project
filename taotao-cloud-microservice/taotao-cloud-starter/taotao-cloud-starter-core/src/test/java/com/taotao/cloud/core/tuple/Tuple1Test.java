package com.taotao.cloud.core.tuple;

import com.taotao.cloud.common.support.tuple.tuple.Tuple1;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple1Test {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWith() {
        Tuple1<String> tuple1 = Tuple1.with("hello");
        log.debug("tuple1:{}", tuple1.toString());
        log.debug("first:{}", tuple1.first);
    }

    @Test
    public void testSwap() {
        Tuple1<String> tuple1 = Tuple1.with("hello");
        log.debug("reverse:{}", tuple1.reverse().toString());
    }
}
