package com.taotao.cloud.core.heaven.support.cache.impl;


import com.taotao.cloud.core.heaven.annotation.NotThreadSafe;
import com.taotao.cloud.core.heaven.reflect.api.IField;
import com.taotao.cloud.core.heaven.reflect.util.Classes;
import java.util.List;

/**
 * 多个字段的缓存
 */
@NotThreadSafe
public class DefaultFieldListCache extends AbstractCache<Class, List<IField>> {

    @Override
    protected List<IField> buildValue(Class key) {
        return Classes.getFields(key);
    }

}
