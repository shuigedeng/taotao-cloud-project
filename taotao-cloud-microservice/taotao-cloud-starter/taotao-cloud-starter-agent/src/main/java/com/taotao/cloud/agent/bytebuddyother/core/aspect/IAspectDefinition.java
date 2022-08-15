package com.taotao.cloud.agent.bytebuddyother.core.aspect;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Created by pphh on 2022/8/4.
 */
public interface IAspectDefinition {

    /**
     * 定义需增强的类
     * Define the class to enhance.
     *
     * @return the class's full name
     */
    ElementMatcher.Junction enhanceClass();

    /**
     * 定义需增强的方法
     * class instance methods matcher.
     *
     * @return methods matcher
     */
    ElementMatcher<MethodDescription> getMethodsMatcher();

    /**
     * 切面增强类
     * method enhancer
     *
     * @return represents a class name, the class instance must instanceof IAspectEnhancer.
     */
    String getMethodsEnhancer();

}
