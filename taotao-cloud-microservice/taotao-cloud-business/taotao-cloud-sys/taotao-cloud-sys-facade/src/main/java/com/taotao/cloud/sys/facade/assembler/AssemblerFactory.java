package com.taotao.cloud.sys.facade.assembler;

import org.springframework.beans.BeanUtils;

/**
 * 装配工厂
 */
public class AssemblerFactory {
    /**
     * 装配工厂 指定任何装配过程 执行装配
     *
     * @param assembler 装配
     * @param source    S对象
     * @param target    T对象
     */
    public <S, T> void assemble(Assembler<S, T> assembler, S source, T target) {
        assembler.assemble(source, target);
    }

    /**
     * 装配工厂 指定任何装配过程 执行转换 S -> T
     *
     * @param assembler 装配
     * @param source    S对象
     * @param type      T Class
     * @return 转换出 T新对象
     */
    public <S, T> T convert(Assembler<S, T> assembler, S source, Class<T> type) {
        T target = BeanUtils.instantiateClass(type);
        assemble(assembler, source, target);
        return target;
    }
}
