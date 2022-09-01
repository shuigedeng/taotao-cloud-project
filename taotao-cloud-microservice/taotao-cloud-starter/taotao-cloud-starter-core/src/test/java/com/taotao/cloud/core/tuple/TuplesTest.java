package com.taotao.cloud.core.tuple;

import static com.taotao.cloud.common.support.tuple.tuple.Tuples.sort;
import static com.taotao.cloud.common.support.tuple.tuple.Tuples.tuple;

import com.taotao.cloud.common.support.tuple.tuple.Tuple0;
import com.taotao.cloud.common.support.tuple.tuple.Tuple1;
import com.taotao.cloud.common.support.tuple.tuple.Tuple2;
import com.taotao.cloud.common.support.tuple.tuple.Tuple3;
import com.taotao.cloud.common.support.tuple.tuple.Tuple4;
import com.taotao.cloud.common.support.tuple.tuple.Tuple5;
import com.taotao.cloud.common.support.tuple.tuple.TupleN;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TuplesTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testTuple0() {
        Tuple0 tuple = tuple();
        log.debug("tuple0:{}", tuple.toString());
    }

    @Test
    public void testTuple1() {
        Tuple1<String> tuple = tuple("test");
        log.debug("tuple1:{}", tuple.toString());
    }

    @Test
    public void testTuple2() {
        Tuple2<String, Integer> tuple = tuple("test", 123);
        log.debug("tuple2:{}", tuple.toString());
    }

    @Test
    public void testTuple3() {
        Tuple3<String, Integer, Boolean> tuple = tuple("test", 123, true);
        log.debug("tuple3:{}", tuple.toString());
    }

    @Test
    public void testTuple4() {
        Tuple4<String, Integer, Boolean, Double> tuple = tuple("test", 123, true, 186.5);
        log.debug("tuple4:{}", tuple.toString());
    }

    @Test
    public void testTuple5() {
        Tuple5<String, Integer, Boolean, Double, Character> tuple = tuple("test", 123, true, 186.5, 'A');
        log.debug("tuple5:{}", tuple.toString());
    }

    @Test
    public void testArrayTupleN() {
        Object[] array = new Object[2];
        array[0] = "hello";
        array[1] = 456;
        TupleN tuple = tuple(array);
        log.debug("tuple:{}", tuple.toString());
    }

    @Test
    public void testSort() {
        List<Tuple2> list = new ArrayList<>();
        list.add(tuple(5, "5"));
        list.add(tuple(2, "2"));
        list.add(tuple(3, "3"));
        list.add(tuple(1, "1"));
        list.add(tuple(4, "4"));
        log.debug("before:{}", list);
        //按第一列Integer类型升序
        sort(list, 0, Integer::compare);
        log.debug("after:{}", list);

        Tuple2[] array = new Tuple2[5];
        array[0] = tuple("5", 5);
        array[1] = tuple("2", 2);
        array[2] = tuple("3", 3);
        array[3] = tuple("1", 1);
        array[4] = tuple("4", 4);
        log.debug("before:{}", Arrays.toString(array));
        //按第一列String类型升序
        sort(array, 0, String::compareTo);
        log.debug("after:{}", Arrays.toString(array));


        List<Tuple2> list2 = new ArrayList<>();
        //空List传入
        sort(list2, 0, Integer::compare);
        list2.add(tuple(5, "5"));
        //size=1的List传入
        sort(list2, 0, Integer::compare);

        Tuple2[] array2 = new Tuple2[0];
        //空数组传入
        sort(array2, 0, String::compareTo);
        array2 = new Tuple2[1];
        array2[0] = tuple("5", 5);
        //length=1的数组传入
        sort(array2, 0, String::compareTo);
        try {
            sort(list, -1, Integer::compare);
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            sort(array, -1, Integer::compare);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
