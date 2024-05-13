package com.github.houbb.rpc.common.support.generic.impl;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.exception.GenericException;
import com.github.houbb.rpc.common.support.generic.GenericService;

import java.util.Arrays;

/**
 * 最简单的泛化调用实现
 *
 * @author shuigedeng
 * @since 0.1.2
 */
public final class FooGenericService implements GenericService {

    private static final Log LOG = LogFactory.getLog(FooGenericService.class);

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        LOG.info("[Generic] method: {}", method);
        LOG.info("[Generic] parameterTypes: {}", Arrays.toString(parameterTypes));
        LOG.info("[Generic] args: {}", args);
        return null;
    }

}
