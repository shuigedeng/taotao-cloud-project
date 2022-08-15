package com.taotao.cloud.agent.bytebuddy.aspect;

import com.taotao.cloud.agent.demo.common.Logger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by pphh on 2022/6/24.
 */
public class AspectProxy {

    public IAspect iAspect;

    @IgnoreForBinding
    public void setiAspect(IAspect iAspect) {
        this.iAspect = iAspect;
    }

    /**
     * Intercept the target instance method with buddybyte
     *
     * @param obj          target class instance.
     * @param allArguments all method arguments
     * @param method       method description.
     * @param zuper        the origin call ref.
     * @return the return value of target instance method.
     * @throws Exception only throw exception because of zuper.call() or unexpected exception in sky-walking ( This is a
     *                   bug, if anything triggers this condition ).
     */
    @RuntimeType
    @BindingPriority(value = 1)
    public Object intercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> zuper,
                            @Origin Method method) throws Throwable {
        Object result = null;
        try {
            iAspect.beforeMethod(obj, method, allArguments, result);
        } catch (Throwable t) {
            String info = String.format("class[%s] before method[%s] intercept failure", obj.getClass(), method.getName());
            Logger.error(info, t);
        }

        Object ret = null;
        try {
            if (null != result) {
                ret = result;
            } else {
                ret = zuper.call();
            }
        } catch (Throwable t) {
            try {
                iAspect.handleMethodException(obj, method, allArguments, t);
            } catch (Throwable t2) {
                String info = String.format("class[%s] handleMethodException method[%s] intercept failure", obj.getClass(), method.getName());
                Logger.error(info, t2);
            }
            throw t;
        } finally {
            try {
                iAspect.afterMethod(obj, method, allArguments, ret);
            } catch (Throwable t3) {
                String info = String.format("class[%s] afterMethod method[%s] intercept failure", obj.getClass(), method.getName());
                Logger.error(info, t3);
            }
        }

        return ret;

    }

}
