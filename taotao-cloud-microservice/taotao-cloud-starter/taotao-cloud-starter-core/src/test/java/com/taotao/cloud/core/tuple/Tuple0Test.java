package com.taotao.cloud.core.tuple;

import com.taotao.cloud.common.support.tuple.tuple.Tuple0;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tuple0Test {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWith() {
        Tuple0 tuple0 = Tuple0.with();
        log.debug("tuple0:{}", tuple0.toString());
    }

    @Test
    public void testSwap() {
        Tuple0 tuple0 = Tuple0.with();
        log.debug("reverse:{}", tuple0.reverse().toString());
    }
}
