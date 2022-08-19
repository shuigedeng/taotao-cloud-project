package com.taotao.cloud.agent.bytebuddyother.agent.trace;

import com.taotao.cloud.agent.bytebuddyother.agent.enhancer.AbstractEnhancer;
import com.taotao.cloud.agent.bytebuddyother.agent.enhancer.EnhancerProxy;
import com.taotao.cloud.agent.bytebuddyother.core.log.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * Created by pphh on 2022/8/4.
 * 通过当前Transformer对类进行字节码增强，会存现NoClassDefFoundError问题。
 * 具体原因分析见博文：
 */
public class TransformerV1 implements AgentBuilder.Transformer {
    public AbstractEnhancer enhancer;

    public TransformerV1(AbstractEnhancer iAspect) {
        this.enhancer = iAspect;
    }

	@Override
	public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
		Logger.info("transformV1 %s...", typeDescription.getTypeName());
		EnhancerProxy proxy = new EnhancerProxy();
		proxy.setEnhancer(enhancer);

		ElementMatcher.Junction<MethodDescription> junction = not(isStatic()).and(enhancer.getMethodsMatcher());
		return builder.method(junction)
			.intercept(MethodDelegation.withDefaultConfiguration()
				.withBinders(Morph.Binder.install(OverrideCallable.class))
				.to(proxy));
	}
}
