package com.taotao.cloud.core.tuple;

import com.taotao.cloud.common.support.tuple.tuple.TupleN;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TupleNTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testWith() {
        TupleN tupleN = TupleN.with(123, 456, "test", "hello", "world", true, 2.5, null, 'B');
        log.debug("tupleN:{}", tupleN.toString());
        Integer first = tupleN.get(0);
        String third = tupleN.get(2);
        log.debug("first:{}", first);
        log.debug("third:{}", third);
    }

    @Test
    public void testSwap() {
        TupleN tupleN = TupleN.with(123, 456, "test", "hello", "world", true, 2.5, null, 'B');
        log.debug("reverse:{}", tupleN.reverse().toString());
    }

}
