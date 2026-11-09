package com.taotao.cloud.mq.consistency.ttc.remoting.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pair 单元测试
 */
public class PairTest {

    @Test
    public void testConstructorAndAccessors() {
        Pair<String, Integer> p = new Pair<>("a", 1);
        assertEquals("a", p.getObject1());
        assertEquals(1, p.getObject2());
    }

    @Test
    public void testStaticFactory() {
        Pair<String, Integer> p = Pair.of("hello", 42);
        assertNotNull(p);
        assertEquals("hello", p.getObject1());
        assertEquals(42, p.getObject2());
    }

    @Test
    public void testSetters() {
        Pair<String, Integer> p = new Pair<>("a", 1);
        p.setObject1("b");
        p.setObject2(2);
        assertEquals("b", p.getObject1());
        assertEquals(2, p.getObject2());
    }

    @Test
    public void testEqualsAndHashCode() {
        Pair<String, Integer> p1 = Pair.of("k", 1);
        Pair<String, Integer> p2 = Pair.of("k", 1);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testNotEqualsWhenFieldDiffers() {
        Pair<String, Integer> p1 = Pair.of("k", 1);
        Pair<String, Integer> p2 = Pair.of("k", 2);
        Pair<String, Integer> p3 = Pair.of("k2", 1);
        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testEqualsNullAndOther() {
        Pair<String, Integer> p = Pair.of("k", 1);
        assertNotEquals(p, null);
        assertNotEquals(p, "string");
    }

    @Test
    public void testEqualsWithNullFields() {
        Pair<String, Integer> p1 = new Pair<>(null, null);
        Pair<String, Integer> p2 = new Pair<>(null, null);
        assertEquals(p1, p2);
    }

    @Test
    public void testToStringContainsFields() {
        Pair<String, Integer> p = Pair.of("abc", 123);
        String s = p.toString();
        assertNotNull(s);
        assertTrue(s.contains("abc"));
        assertTrue(s.contains("123"));
    }
}
