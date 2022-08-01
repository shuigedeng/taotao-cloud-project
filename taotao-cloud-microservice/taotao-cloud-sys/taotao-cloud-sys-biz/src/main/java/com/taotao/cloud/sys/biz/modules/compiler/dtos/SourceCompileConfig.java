package com.taotao.cloud.sys.biz.modules.compiler.dtos;

import lombok.Data;

/**
 * java 源码编译配置
 */
@Data
public class SourceCompileConfig {
    /**
     * 类名
     */
    private String className;
    /**
     * 源码
     */
    private String source;
    /**
     * 使用哪个类加载器, 编译时如果有依赖, 会使用这个类加载器中的类 ,编译成功后放到类加载器中
     */
    private String classloaderName;

    public SourceCompileConfig() {
    }

    public SourceCompileConfig(String className, String source) {
        this.className = className;
        this.source = source;
    }
}
