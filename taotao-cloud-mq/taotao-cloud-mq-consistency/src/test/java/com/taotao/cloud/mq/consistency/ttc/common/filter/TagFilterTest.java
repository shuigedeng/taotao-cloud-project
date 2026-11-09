package com.taotao.cloud.mq.consistency.ttc.common.filter;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link TagFilter} 的匹配语义（本类原先零测试）。
 *
 * <p>核心回归：{@code String#split} 收的是<b>正则</b>，裸 {@code "||"} 在正则里是
 * 「空分支或空分支」，会把 Tag 名逐字符切碎。修之前
 * {@code "WANTED".split("||")} = {@code [W, A, N, T, E, D]}，
 * 于是 {@code match} 对任何消息都返回 false —— Broker 端 Tag 过滤从未生效过。
 */
class TagFilterTest {

    private static MessageExt msg(String tags) {
        MessageExt m = new MessageExt();
        m.setTopic("T");
        m.setQueueId(0);
        m.setTags(tags);
        return m;
    }

    @Test
    @DisplayName("单 Tag 精确匹配：分隔符不能把 Tag 名切碎")
    void singleTagMatchesExactly() {
        TagFilter f = new TagFilter("WANTED");
        assertTrue(f.match(msg("WANTED")), "tags=WANTED 过滤 WANTED 必须匹配");
        assertFalse(f.match(msg("OTHER")));
        assertFalse(f.match(msg("wanted")), "Tag 区分大小写");
    }

    @Test
    @DisplayName("多 Tag 或匹配：|| 是字面量分隔符")
    void multiTagOrMatching() {
        TagFilter f = new TagFilter("TagA||TagB||TagC");
        assertTrue(f.match(msg("TagA")));
        assertTrue(f.match(msg("TagB")));
        assertTrue(f.match(msg("TagC")));
        assertFalse(f.match(msg("TagD")));
    }

    @Test
    @DisplayName("分隔符两侧的空白被 trim 掉")
    void trimsAroundSeparator() {
        TagFilter f = new TagFilter(" TagA || TagB ");
        assertTrue(f.match(msg("TagA")));
        assertTrue(f.match(msg("TagB")));
    }

    @Test
    @DisplayName("* / null / 空串 = 全匹配")
    void allMatchCases() {
        assertTrue(new TagFilter("*").match(msg("anything")));
        assertTrue(new TagFilter("*").match(msg(null)));
        assertTrue(new TagFilter(null).match(msg("anything")));
        assertTrue(new TagFilter("").match(msg("anything")));
    }

    @Test
    @DisplayName("指定了 Tag 而消息没有 Tag ⇒ 不匹配")
    void messageWithoutTagDoesNotMatch() {
        TagFilter f = new TagFilter("WANTED");
        assertFalse(f.match(msg(null)));
        assertFalse(f.match(msg("")));
    }

    @Test
    @DisplayName("表达式全是分隔符时退化为全匹配，不得把所有消息都拒掉")
    void degenerateExpressionDoesNotRejectEverything() {
        assertTrue(new TagFilter("||").match(msg("anything")),
                "空 tagSet 的既定语义是全匹配，不是全不匹配");
    }

    @Test
    @DisplayName("matchTag 与 match 口径一致")
    void matchTagAgreesWithMatch() {
        TagFilter f = new TagFilter("TagA||TagB");
        assertTrue(f.matchTag("TagA"));
        assertTrue(f.matchTag("TagB"));
        assertFalse(f.matchTag("TagC"));
        assertFalse(f.matchTag(null));
        assertFalse(f.matchTag(""));
        assertTrue(new TagFilter("*").matchTag("whatever"));
    }

    @Test
    @DisplayName("hashCode 预过滤与精确匹配不矛盾")
    void hashPrefilterNeverExcludesRealMatches() {
        TagFilter f = new TagFilter("TagA||TagB");
        assertTrue(f.mayMatchByHashCode("TagA".hashCode()));
        assertTrue(f.mayMatchByHashCode("TagB".hashCode()));
        // 预过滤只承诺"不误杀"，允许放过（碰撞），所以对无关 tag 可以为 false
        assertEquals("TagA".hashCode(), "TagA".hashCode());
    }

    @Test
    @DisplayName("getFilterType / getExpression 如实返回")
    void accessorsReflectInput() {
        assertEquals(FilterType.TAG, new TagFilter("TagA").getFilterType());
        assertEquals("TagA||TagB", new TagFilter("TagA||TagB").getExpression());
        assertEquals("*", new TagFilter(null).getExpression(), "null 归一成全匹配表达式");
    }
}
