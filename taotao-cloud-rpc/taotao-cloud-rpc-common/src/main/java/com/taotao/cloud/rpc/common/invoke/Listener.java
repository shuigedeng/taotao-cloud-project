package com.taotao.cloud.rpc.common.invoke;

import com.taotao.cloud.rpc.common.invoke.Invoker;

/**
 * 监听器接口
 * <p>
 * 用于在 Invoker/Exporter 生命周期关键点插入自定义逻辑。
 */
public interface Listener {

    /**
     * 导出时触发
     */
    void exported(Object exporter);

    /**
     * 引用时触发
     */
    void referred(Invoker<?> invoker);

    /**
     * 销毁时触发
     */
    void destroyed(Invoker<?> invoker);

    /**
     * 默认空实现
     */
    Listener DEFAULT = new Listener() {
        @Override
        public void exported(Object exporter) {}
        @Override
        public void referred(Invoker<?> invoker) {}
        @Override
        public void destroyed(Invoker<?> invoker) {}
    };
}
