package com.taotao.cloud.core.tuple;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TupleTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testToString() {
        TupleN tuple = TupleN.with("hello", 123, true, 186.5, null);
        String one = tuple.get(0);
        int two = tuple.get(1);
        log.debug("one:{}", one);
        log.debug("two:{}", two);
        log.debug("toString:{}", tuple.toString());
    }

    @Test
    public void testForeach() {
        TupleN tuple = TupleN.with("hello", 123, true, 186.5, null);
        log.debug("foreach:");
        tuple.forEach(o -> log.debug(Objects.toString(o)));
        for (Object object : tuple) {
            log.debug(Objects.toString(object));
        }
    }

    @Test
    public void testAdd() {
        Tuple1 tuple1 = Tuple1.with("hello");
        Tuple2 tuple2 = Tuple2.with("world", "!");
        Tuple3 tuple3 = Tuple3.with(1, 2, null);
        log.debug("add0:{}", tuple1.add(new Tuple1[]{}).toString());
        log.debug("add1:{}", tuple1.add(tuple2).toString());
        log.debug("add2:{}", tuple1.add(tuple2, tuple3).toString());
    }

    @Test
    public void testSwap() {
        TupleN tuple = TupleN.with("hello", 123, true, null, 186.5);
        log.debug("reverse:{}", tuple.reverse().toString());
    }

    @Test
    public void testTuple0() {
        Tuple0 tuple0 = Tuple0.with();
        Tuple0 tuple01 = Tuple0.with();
        log.debug("tuple0:{}", tuple0 == tuple01);
        Tuple1 tuple1 = Tuple1.with("123");
        log.debug("0+1:{}", tuple0.add(tuple1));
    }

    @Test
    public void testRepeat() {
        Tuple2 tuple2 = Tuple2.with("a", null);
        log.debug("repeat0:{}", tuple2.repeat(0).toString());
        log.debug("repeat3:{}", tuple2.repeat(3).toString());

        try {
            tuple2.repeat(-1);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testTuple2() {
        Tuple2 tuple2 = Tuple2.with("test", 123);
        log.debug("first:{}", tuple2.first);//test
        log.debug("second:{}", tuple2.second);//123
    }

    @Test
    public void testNull() {
        Tuple2 tuple2 = Tuple2.with("test", null);
        log.debug("test null:{}", tuple2.toString());
        log.debug("null:{}", tuple2.second);
    }

    @Test
    public void testSub() {
        TupleN tupleN = TupleN.with(0, 1, 2, 3, 4, 5, 6);
        log.debug("sub1:{}", tupleN.subTuple(0, 0).toString());
        log.debug("sub2:{}", tupleN.subTuple(0, 1).toString());
        log.debug("sub3:{}", tupleN.subTuple(0, 2).toString());
        log.debug("sub4:{}", tupleN.subTuple(0, 3).toString());
        log.debug("sub5:{}", tupleN.subTuple(0, 4).toString());
        log.debug("sub6:{}", tupleN.subTuple(0, 5).toString());
        log.debug("sub7:{}", tupleN.subTuple(0, tupleN.size() - 1).toString());
        try {
            tupleN.subTuple(-1, 0);
        } catch (Exception e) {
            log.error("", e);
        }

        try {
            tupleN.subTuple(0, -1);
        } catch (Exception e) {
            log.error("", e);
        }

        try {
            tupleN.subTuple(0, 1000);
        } catch (Exception e) {
            log.error("", e);
        }

        try {
            tupleN.subTuple(5, 3);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testStream() {
        TupleN tupleN = TupleN.with("hello", 123, true, null, 186.5);
        tupleN.stream().forEach(o -> log.debug("元素:{}", o));
        tupleN.parallelStream().forEach(o -> log.debug("元素:{}", o));
    }

    @Test
    public void testToList() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        log.debug("toList:{}", tuple2.toList());
    }

    @Test
    public void testToArray() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        log.debug("toArray:{}", Arrays.toString(tuple2.toArray()));
    }

    @Test
    public void testContains() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        log.debug("contains:{}", tuple2.contains(123));
        log.debug("contains:{}", tuple2.contains(456));
    }

    @Test
    public void testIterator() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        Iterator<Object> iterator = tuple2.iterator();
        while (iterator.hasNext()) {
            log.debug("value:{}", iterator.next());
        }
    }

    @Test
    public void testEquals() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        log.debug("equals null:{}", tuple2.equals(null));
        Tuple0 tuple0 = Tuple0.with();
        Tuple0 tuple01 = Tuple0.with();
        log.debug("equals same:{}", tuple0.equals(tuple01));
        log.debug("equals not same:{}", tuple2.equals(tuple0));
        log.debug("equals not same class:{}", tuple2.equals("123"));
    }

    @Test
    public void testHashCode() {
        Tuple2 tuple2 = Tuple2.with("hello", 123);
        log.debug("hashCode:{}", tuple2.hashCode());
    }
}
