package com.taotao.cloud.agent.javaassist;

import com.taotao.cloud.agent.demo.common.Logger;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by pphh on 2022/6/23.
 */
public class Transformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        Logger.info("transforming class = %s", className);

        byte[] bytes = null;
        if (className.equals("com/pphh/demo/api/Greeting")) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get("com.pphh.demo.api.Greeting");
                CtMethod m = cc.getDeclaredMethod("sayHello");
                m.insertBefore("{ System.out.println(\"begin of sayhello()\"); }");
                m.insertAfter("{ System.out.println(\"end of sayhello()\"); }");
                bytes = cc.toBytecode();
                Logger.info("class transformed = %s", className);
            } catch (Exception e) {
                Logger.info("a exception happened when transforming class = %s", className);
            }
        } else {
            Logger.info("class transforming skip, not the target class.");
        }

        return bytes;
    }
}
