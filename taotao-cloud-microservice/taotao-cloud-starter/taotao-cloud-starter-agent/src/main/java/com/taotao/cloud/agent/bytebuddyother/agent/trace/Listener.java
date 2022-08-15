package com.taotao.cloud.agent.bytebuddyother.agent.trace;

import com.taotao.cloud.agent.bytebuddyother.core.log.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * Created by pphh on 2022/8/4.
 */
public class Listener implements AgentBuilder.Listener {
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean bLoaded) {
        Logger.debug("Enhance class {%s} onDiscovery, loaded = %s", typeName, bLoaded);
    }

    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean bLoaded, DynamicType dynamicType) {
        Logger.debug("On Transformation class {%s}, loaded = %s", typeDescription.getName(), bLoaded);
    }

    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean bLoaded) {
        Logger.debug("Enhance class {%s} onIgnored, loaded = %s", typeDescription, bLoaded);
    }

    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean bLoaded, Throwable throwable) {
        Logger.error("Enhance class {%s} error, loaded = %s, exception msg = %s", typeName, bLoaded, throwable.getMessage());
    }

    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule javaModule, boolean bLoaded) {
        Logger.debug("Enhance class {%s} onComplete, loaded = %s", typeName, bLoaded);
    }
}
