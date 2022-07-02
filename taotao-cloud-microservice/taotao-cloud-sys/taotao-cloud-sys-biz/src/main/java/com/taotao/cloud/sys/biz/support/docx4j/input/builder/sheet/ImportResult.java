package com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet;


import com.taotao.cloud.sys.biz.support.docx4j.input.InputConstants;
import com.taotao.cloud.sys.biz.support.docx4j.input.utils.TrConsumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * excel sheet解析结果
 */
public class ImportResult<T> {
    /**
     * 跳过解析记录数
     */
    private final List<Integer> skip = new ArrayList<>();
    /**
     * 空行记录数
     */
    private final List<Integer> empty = new ArrayList<>();
    /**
     * 原始记录值
     */
    private final Map<Integer, Map<Integer, String>> origin = new HashMap<>();
    /**
     * 校验合法记录数 无序
     */
    private final Map<Integer, T> validRecords = new HashMap<>();
    /**
     * 校验非法源记录
     */
    private final Map<Integer, T> invalidRecords = new HashMap<>();
    /**
     * 校验非法记录数 列有序
     */
    private final Map<Integer, List<String>> invalidRecordMessage = new TreeMap<>();

    ImportResult() {
    }

    /**
     * 添加不合法记录
     * @param index    索引
     * @param t        数据
     * @param messages 不合法原因
     */
    public void addInvalidRecord(int index, T t, List<String> messages) {
        this.invalidRecords.put(index, t);
        this.invalidRecordMessage.merge(index, messages, (o, n) -> {
            o.addAll(n);
            return o;
        });
    }

    /**
     * 遍历有效的数据 遍历删除
     * @param consumer 合法元素移除标识
     * @return {@link ImportResult <T>}
     */
    public ImportResult<T> remove(BiConsumer<T, List<String>> consumer) {
        this.validRecords.entrySet()
            .removeIf(it -> {
                List<String> messages = new ArrayList<>();
                consumer.accept(it.getValue(), messages);

                return
                    Optional.of(messages)
                        // 若存在错误信息
                        .filter(m -> !m.isEmpty())
                        // 则加入错误信息列表
                        .map(m -> {
                            this.addInvalidRecord(it.getKey(), it.getValue(), m);
                            return true;
                        })
                        // 合法数据
                        .orElse(false);
            });

        return this;
    }

    /**
     * 遍历有效数据满足添加做删除
     * @param supplier 判断有效基准数据
     * @param consumer 移除元素消费
     * @param <U>      有效数据类型
     * @return {@link ImportResult}
     */
    public <U> ImportResult<T> remove(Supplier<U> supplier, TrConsumer<T, List<String>, U> consumer) {
        if (this.hasValid()) {
            // 当且仅当存在有效数据时 才做数据初始化
            U u = supplier.get();
            this.remove((t, m) -> consumer.accept(t, m, u));
        }

        return this;
    }

    /**
     * 移除重复数据
     * @param function 重复标识
     * @param <R>      重复标识类型
     * @return {@link ImportResult <T>}
     */
    public <R> ImportResult<T> removeIfRepeated(Function<T, R> function, String message) {
        return
            this.remove((Supplier<Set<R>>) HashSet::new, (t, m, s) -> {
                if (!s.add(function.apply(t))) {
                    m.add(message);
                }
            });
    }

    /**
     * 满足给定的条件执行过程
     * @param predicate 任意条件
     * @param consumer  合法记录消费
     * @return {@link ImportSummary}
     */
    public ImportResult<T> onAny(Predicate<ImportResult<T>> predicate, Consumer<List<T>> consumer) {
        if (predicate.test(this)) {
            consumer.accept(new ArrayList<>(this.validRecords.values()));
        }

        return this;
    }

    /**
     * 当且仅当不存在错误数据且存在有效数据时执行
     * @param consumer 合法记录消费
     * @return {@link ImportSummary}
     */
    public ImportResult<T> onAllValid(Consumer<List<T>> consumer) {
        // 当且仅当不存在错误数据且存在有效数据时执行
        return this.onAny(t -> !t.hasInvalid() && t.hasValid(), consumer);
    }

    /**
     * 当且仅当存在有效数据时执行
     * @param consumer 合法记录消费
     * @return {@link ImportSummary}
     */
    public ImportResult<T> onValid(Consumer<List<T>> consumer) {
        // 仅当存在有效数据时执行
        return this.onAny(ImportResult::hasValid, consumer);
    }

    /**
     * 是否存在校验不通过的数据
     * @return true/false
     */
    public boolean hasInvalid() {
        return !this.invalidRecordMessage.isEmpty();
    }

    /**
     * 是否存在有效数据 可能是个空excel
     * @return true/false
     */
    public boolean hasValid() {
        return !this.validRecords.isEmpty();
    }

    /**
     * 汇总信息
     * @return {@link ImportSummary}
     */
    public ImportSummary getSummary() {
        return this.getSummary(InputConstants.SEMICOLON);
    }

    /**
     * 汇总信息
     * @return {@link ImportSummary}
     */
    public ImportSummary getSummary(String separator) {
        return new ImportSummary(this, separator);
    }

    /**
     * 详情信息
     * @return {@link ImportDetail}
     */
    public ImportDetail<T> getDetail() {
        return this.getDetail(InputConstants.SEMICOLON);
    }

    /**
     * 详情信息
     * @param separator 多个错误信息分隔符
     * @return {@link ImportDetail}
     */
    public ImportDetail<T> getDetail(String separator) {
        return new ImportDetail<>(this, separator);
    }

    /**
     * 添加跳过行
     * @param index 索引
     */
    void skip(int index) {
        this.skip.add(index);
    }

    /**
     * 条件空行
     * @param index 索引
     */
    void addEmpty(int index) {
        this.empty.add(index);
    }

    /**
     * 添加合法记录
     * @param index 索引
     * @param t     记录值
     */
    void addValidRecord(int index, T t) {
        this.validRecords.put(index, t);
    }

    /**
     * 添加记录
     * @param index    行索引
     * @param t        实体
     * @param messages 非法信息
     */
    void addRecord(int index, T t, Map<Integer, String> origin, List<String> messages) {
        this.origin.put(index, origin);
        if (messages.isEmpty()) {
            this.addValidRecord(index, t);
        } else {
            this.addInvalidRecord(index, t, messages);
        }
    }

	public List<Integer> getSkip() {
		return skip;
	}

	public List<Integer> getEmpty() {
		return empty;
	}

	public Map<Integer, Map<Integer, String>> getOrigin() {
		return origin;
	}

	public Map<Integer, T> getValidRecords() {
		return validRecords;
	}

	public Map<Integer, T> getInvalidRecords() {
		return invalidRecords;
	}

	public Map<Integer, List<String>> getInvalidRecordMessage() {
		return invalidRecordMessage;
	}
}
