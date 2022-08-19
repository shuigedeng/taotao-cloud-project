package com.taotao.cloud.agent.bytebuddyother.agent.trace;

import com.taotao.cloud.agent.bytebuddyother.agent.enhancer.EnhancerProxy;
import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectDefinition;
import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectEnhancer;
import com.taotao.cloud.agent.bytebuddyother.core.loader.EnhancerInstanceLoader;
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
 * Created by pphh on 2022/8/5.
 * 通过当前Transformer对类进行字节码增强，通过EnhancerInstanceLoader创建enhancer实例，解决了NoClassDefFoundError问题。
 * 具体原因分析见博文：
 */
public class TransformerV3 implements AgentBuilder.Transformer {

    public String aspectClz;

    public TransformerV3(String aspectClz) {
        this.aspectClz = aspectClz;
    }

	@Override
	public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
		Logger.info("transformV3 %s...", typeDescription.getTypeName());

		EnhancerProxy proxy = new EnhancerProxy();
		ElementMatcher<MethodDescription> methodsMatcher = null;
		try {
			IAspectDefinition aspectDefinition = EnhancerInstanceLoader.load(this.aspectClz, classLoader);
			methodsMatcher = aspectDefinition.getMethodsMatcher();

			String enhancerClz = aspectDefinition.getMethodsEnhancer();
			IAspectEnhancer enhancer = EnhancerInstanceLoader.load(enhancerClz, classLoader);
			proxy.setEnhancer(enhancer);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Logger.error("failed to initialized the proxy %s", e.toString());
		}

		if (methodsMatcher == null) {
			Logger.error("methodsMatcher is null");
			return null;
		}

		ElementMatcher.Junction<MethodDescription> junction = not(isStatic()).and(methodsMatcher);
		return builder.method(junction)
			.intercept(MethodDelegation.withDefaultConfiguration()
				.withBinders(Morph.Binder.install(OverrideCallable.class))
				.to(proxy));
	}
}
