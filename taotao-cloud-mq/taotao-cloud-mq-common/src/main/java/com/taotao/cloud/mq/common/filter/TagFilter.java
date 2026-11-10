package com.taotao.cloud.mq.common.filter;

import com.taotao.cloud.mq.common.filter.FilterType;
import com.taotao.cloud.mq.common.filter.MessageFilter;
import com.taotao.cloud.mq.common.message.MessageExt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tag 标签过滤器（对标 RocketMQ TagFilter）.
 * <p>
 * 支持的过滤表达式语法：
 * <ul>
 *   <li>{@code *} — 全匹配，所有消息都通过</li>
 *   <li>{@code TagA} — 单 Tag 精确匹配</li>
 *   <li>{@code TagA||TagB||TagC} — 多 Tag 或匹配</li>
 * </ul>
 * <p>
 * Broker 端先按 Tag hashCode 预过滤（快速排除），客户端再精确匹配 Tag 字符串。
 * 这与 RocketMQ 的实现方式一致。
 */
public class TagFilter implements MessageFilter {

    /** 全匹配表达式 */
    private static final String ALL_MATCH = "*";

    /** 多 Tag 分隔符（字面量，仅用于文档/展示） */
    private static final String TAG_SEPARATOR = "||";

    /**
     * 多 Tag 分隔符的<b>正则</b>形态。
     * <p>
     * {@link String#split(String)} 收的是正则，不是字面量。裸 {@code "||"} 在正则里
     * 是「空分支 或 空分支」，匹配的是空串而不是两个竖线，于是
     * {@code "WANTED".split("||")} 会碎成 {@code [W, A, N, T, E, D]}、
     * {@code "TagA||TagB"} 会碎成 {@code [T, a, g, A, |, |, T, a, g, B]}，
     * 产出的 tagSet 里根本没有完整的 Tag 名 ⇒ {@code match} 对任何消息都返回 false
     * ⇒ <b>Broker 端 Tag 过滤永远匹配不上，带订阅的消费者一条都收不到</b>。
     * <p>
     * 客户端 {@code DefaultMQPushConsumer.clientSideTagFilter} 用的是 {@code "\\|\\|"}，
     * 两侧原本并不等价（客户端对、Broker 错），这处不一致就是缺陷的藏身处。
     */
    private static final String TAG_SEPARATOR_REGEX = "\\|\\|";

    private final String expression;
    private final Set<String> tagSet;
    private final boolean allMatch;

    /**
     * 创建 Tag 过滤器。
     *
     * @param expression 过滤表达式，null 或空字符串等同于全匹配
     */
    public TagFilter(String expression) {
        this.expression = (expression == null || expression.isEmpty()) ? ALL_MATCH : expression.trim();
        this.allMatch = ALL_MATCH.equals(this.expression);

        if (this.allMatch) {
            this.tagSet = Collections.emptySet();
        } else {
            String[] tags = this.expression.split(TAG_SEPARATOR_REGEX);
            this.tagSet = new HashSet<>();
            for (String tag : tags) {
                String trimmed = tag.trim();
                if (!trimmed.isEmpty()) {
                    this.tagSet.add(trimmed);
                }
            }
        }
    }

    @Override
    public boolean match(MessageExt msg) {
        if (allMatch) {
            return true;
        }
        if (tagSet.isEmpty()) {
            return true;
        }
        String msgTag = msg.getTags();
        if (msgTag == null || msgTag.isEmpty()) {
            // 消息无 Tag，但订阅指定了 Tag → 不匹配
            return false;
        }
        return tagSet.contains(msgTag);
    }

    /**
     * 快速预过滤：基于 Tag hashCode 判断是否可能匹配。
     * <p>
     * 用于 Broker 端的 ConsumeQueue 过滤优化。
     * 返回 true 表示可能匹配（需要客户端精确验证），false 表示一定不匹配。
     *
     * @param tagHashCode 消息 Tag 的 hashCode
     * @return 是否可能匹配
     */
    public boolean mayMatchByHashCode(int tagHashCode) {
        if (allMatch) {
            return true;
        }
        for (String tag : tagSet) {
            if (tag.hashCode() == tagHashCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断消息 Tag 是否匹配（精确匹配）。
     * 用于客户端收到消息后的二次验证。
     */
    public boolean matchTag(String msgTag) {
        if (allMatch) {
            return true;
        }
        if (msgTag == null || msgTag.isEmpty()) {
            return false;
        }
        return tagSet.contains(msgTag);
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.TAG;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    /**
     * 获取所有匹配的 Tag 集合。
     */
    public Set<String> getTagSet() {
        return Collections.unmodifiableSet(tagSet);
    }

    /**
     * 是否为全匹配模式。
     */
    public boolean isAllMatch() {
        return allMatch;
    }

    /**
     * 获取所有 Tag hashCode 数组（用于 Broker 端 ConsumeQueue 预过滤）。
     */
    public int[] getTagHashCodes() {
        if (allMatch || tagSet.isEmpty()) {
            return new int[0];
        }
        return tagSet.stream()
                .mapToInt(String::hashCode)
                .toArray();
    }

    @Override
    public String toString() {
        return "TagFilter{expression='" + expression + "', allMatch=" + allMatch
                + ", tags=" + tagSet + "}";
    }
}
