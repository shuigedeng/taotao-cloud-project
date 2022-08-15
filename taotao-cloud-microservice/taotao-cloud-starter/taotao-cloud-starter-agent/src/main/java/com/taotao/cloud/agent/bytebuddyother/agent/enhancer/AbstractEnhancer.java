package com.taotao.cloud.agent.bytebuddyother.agent.enhancer;

import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectDefinition;
import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectEnhancer;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.reflect.Method;

/**
 * Created by pphh on 2022/8/4.
 */
public abstract class AbstractEnhancer implements IAspectEnhancer, IAspectDefinition {

    @Override
    public ElementMatcher.Junction enhanceClass() {
        return null;
    }

    @Override
    public ElementMatcher<MethodDescription> getMethodsMatcher() {
        return null;
    }

    @Override
    public String getMethodsEnhancer() {
        return null;
    }

    @Override
    public void beforeMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object result) throws Throwable {

    }

    @Override
    public Object afterMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return null;
    }

    @Override
    public void handleMethodException(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {

    }

}
