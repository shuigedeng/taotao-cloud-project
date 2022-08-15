package com.taotao.cloud.agent.bytebuddyother.plugin;

import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectDefinition;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.isInterface;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * Created by pphh on 2022/8/5.
 */
public class TomcatAspect implements IAspectDefinition {

    private static final String ENHANCE_CLASS = "org.apache.catalina.core.StandardHostValve";
    private static final String ENHANCE_METHOD = "invoke";
    private static final String INTERCEPT_CLASS = "com.phantom.plugin.TomcatEnhancer";

    @Override
    public ElementMatcher.Junction enhanceClass() {
        return named(ENHANCE_CLASS).and(not(isInterface()));
    }

    @Override
    public ElementMatcher<MethodDescription> getMethodsMatcher() {
        return named(ENHANCE_METHOD);
    }

    @Override
    public String getMethodsEnhancer() {
        return INTERCEPT_CLASS;
    }

}
