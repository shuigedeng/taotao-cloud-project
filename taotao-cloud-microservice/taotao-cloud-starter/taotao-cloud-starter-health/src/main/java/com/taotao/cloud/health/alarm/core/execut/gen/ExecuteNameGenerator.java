package com.taotao.cloud.health.alarm.core.execut.gen;

public class ExecuteNameGenerator {

    /**
     * 报警执行单元的 name 默认生成规则:
     * <p>
     * - 类名
     * - 去掉后缀的 Execute
     * - 转大写
     *
     * @param clz
     * @return
     */
    public static String genExecuteName(Class clz) {
        String simpleName = clz.getSimpleName();
        int index = simpleName.indexOf("Execute");
        if (index > 0) {
            return simpleName.substring(0, index).toUpperCase();
        } else {
            return simpleName;
        }
    }

}
