package com.taotao.cloud.core.heaven.reflect.api;

import java.util.List;

/**
 * 类信息接口
 */
public interface IClass extends IMember {

    /**
     * 字段列表
     * @return 字段列表
     */
    List<IField> fields();

    /**
     * 方法列表
     * @return 方法列表
     */
    List<IMethod> methods();

}
