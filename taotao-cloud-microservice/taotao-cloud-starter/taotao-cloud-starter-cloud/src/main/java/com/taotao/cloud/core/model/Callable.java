package com.taotao.cloud.core.model;

/**
 * 通用回调定义
 * @author: chejiangyi
 * @version: 2019-07-30 16:42
 **/
public class Callable {
    public interface Action0 {
        void invoke();
    }
    public interface Action1<T1> {
        void invoke(T1 t1);
    }
    public interface Action2<T1,T2> {
        void invoke(T1 t1,T2 t2);
    }
    public interface Action3<T1,T2,T3> {
        void invoke(T1 t1,T2 t2,T3 t3);
    }
    public interface Func0<T0> {
        T0 invoke();
    }
    public interface Func1<T0,T1> {
        T0 invoke(T1 t1);
    }
    public interface Func2<T0,T1,T2> {
        T0 invoke(T1 t1,T2 t2);
    }
}
