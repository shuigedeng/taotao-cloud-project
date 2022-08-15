package com.taotao.cloud.agent.bytebuddy;

import com.taotao.cloud.agent.demo.common.Logger;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Created by pphh on 2022/6/24.
 */
public class Agent {

    public static void premain(String args, Instrumentation inst) {
        Logger.info("loading agent..");

        // 对Greeting类字节码进行增强转换
        Listener listener = new Listener();
        new AgentBuilder.Default().type(named("com.pphh.demo.api.Greeting"))
                .transform(new Tranzformer())
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(listener)
                .installOn(inst);

        Logger.info("agent has been loaded.");
    }

    public static void agentmain(String args, Instrumentation inst) {
    }
}
