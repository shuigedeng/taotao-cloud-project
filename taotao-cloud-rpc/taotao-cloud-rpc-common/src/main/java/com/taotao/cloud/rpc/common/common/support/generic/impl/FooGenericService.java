package com.taotao.cloud.rpc.common.common.support.generic.impl;

import com.taotao.cloud.rpc.common.common.exception.GenericException;
import com.taotao.cloud.rpc.common.common.support.generic.GenericService;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 最简单的泛化调用实现
 *
 * @author shuigedeng
 * @since 0.1.2
 */
public final class FooGenericService implements GenericService {

    private static final Logger LOG = LoggerFactory.getLogger(FooGenericService.class);

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
//        LOG.info("[Generic] method: {}", method);
//        LOG.info("[Generic] parameterTypes: {}", Arrays.toString(parameterTypes));
//        LOG.info("[Generic] args: {}", args);
        return null;
    }

}
