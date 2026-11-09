package com.taotao.cloud.mq.consistency.ttc.common.filter;

/**
 * 消息过滤类型枚举（对标 RocketMQ FilterType）.
 * <p>
 * 支持两种过滤方式：
 * <ul>
 *   <li>{@link #TAG} — Tag 标签过滤（精确匹配，Broker 端先按 hashCode 预过滤）</li>
 *   <li>{@link #SQL92} — SQL92 属性过滤（表达式求值，Broker 端解析执行）</li>
 * </ul>
 */
public enum FilterType {

    /**
     * Tag 标签过滤（默认）.
     * <p>
     * 过滤表达式语法：
     * <ul>
     *   <li>{@code TagA} — 单 Tag 精确匹配</li>
     *   <li>{@code TagA||TagB||TagC} — 多 Tag 或匹配</li>
     *   <li>{@code *} — 全匹配（不过滤）</li>
     * </ul>
     */
    TAG,

    /**
     * SQL92 属性过滤.
     * <p>
     * 过滤表达式语法（SQL92 子集）：
     * <ul>
     *   <li>{@code IS NULL / IS NOT NULL} — 属性存在性判断</li>
     *   <li>{@code = / <>} — 等于/不等于</li>
     *   <li>{@code > / < / >= / <=} — 数值比较</li>
     *   <li>{@code BETWEEN x AND y} — 区间判断</li>
     *   <li>{@code IN ('a', 'b')} — 集合包含</li>
     *   <li>{@code AND / OR} — 逻辑组合</li>
     * </ul>
     */
    SQL92;

    /**
     * 根据字符串值解析 FilterType，不区分大小写。
     * 无效值返回 {@link #TAG}（默认）。
     */
    public static FilterType fromString(String value) {
        if (value == null || value.isEmpty()) {
            return TAG;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TAG;
        }
    }
}
