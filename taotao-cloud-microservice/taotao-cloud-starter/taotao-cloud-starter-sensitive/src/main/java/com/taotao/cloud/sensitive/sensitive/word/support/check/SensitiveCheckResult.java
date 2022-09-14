package com.taotao.cloud.sensitive.sensitive.word.support.check;

/**
 * 敏感信息监测接口结果
 *
 * 可以使用责任链的模式，循环调用。
 */
public class SensitiveCheckResult {

    /**
     * 下标
     */
    private int index;

    /**
     * 检测类
     */
    private Class<? extends ISensitiveCheck> checkClass;

    /**
     * 实例化
     * @param index 返回索引
     * @param checkClass 验证类
     * @return 结果
     */
    public static SensitiveCheckResult of(final int index,
                                          final Class<? extends ISensitiveCheck> checkClass) {
        SensitiveCheckResult result = new SensitiveCheckResult();
        result.index(index).checkClass(checkClass);
        return result;
    }

    public int index() {
        return index;
    }

    public SensitiveCheckResult index(int index) {
        this.index = index;
        return this;
    }

    public Class<? extends ISensitiveCheck> checkClass() {
        return checkClass;
    }

    public SensitiveCheckResult checkClass(Class<? extends ISensitiveCheck> checkClass) {
        this.checkClass = checkClass;
        return this;
    }

    @Override
    public String toString() {
        return "SensitiveCheckResult{" +
                "index=" + index +
                ", checkClass=" + checkClass +
                '}';
    }

}
