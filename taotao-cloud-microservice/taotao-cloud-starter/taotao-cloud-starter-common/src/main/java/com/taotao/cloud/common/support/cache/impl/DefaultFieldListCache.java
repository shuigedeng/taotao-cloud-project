package com.taotao.cloud.common.support.cache.impl;


import com.taotao.cloud.common.support.reflect.api.IField;
import com.taotao.cloud.common.support.reflect.util.Classes;

import java.util.List;

/**
 * 多个字段的缓存
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:07:47
 */
public class DefaultFieldListCache extends AbstractCache<Class, List<IField>> {

    @Override
    protected List<IField> buildValue(Class key) {
        return Classes.getFields(key);
    }

}
