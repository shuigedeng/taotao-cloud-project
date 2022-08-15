package com.taotao.cloud.agent.asm;

import com.taotao.cloud.agent.demo.common.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by pphh on 2022/6/24.
 */
public class Transformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        Logger.info("transforming class = %s", className);

        byte[] bytes = null;
        if (className.equals("com/pphh/demo/api/Greeting")) {
            try {
                ClassReader reader = new ClassReader(className);

                // COMPUTE_MAXS 自动计算局部变量表和操作数栈，有一定效率损耗，但比COMPUTE_FRAMES速度快
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassVisitor classVisitor = new GreetingClassVisitor(writer);
                reader.accept(classVisitor, ClassReader.SKIP_DEBUG);

                bytes = writer.toByteArray();
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
