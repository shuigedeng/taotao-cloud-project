package com.taotao.cloud.agent.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by pphh on 2022/6/24.
 */
public class GreetingClassVisitor extends ClassVisitor implements Opcodes {

    public GreetingClassVisitor(ClassVisitor visitor) {
        super(ASM5, visitor);
    }

    @Override
    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access,
                                     String name,
                                     String desc,
                                     String signature,
                                     String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature,
                exceptions);

        // 跳过Greeting类中的构造函数
        if (!name.equals("<init>") && methodVisitor != null) {
            methodVisitor = new GreetingMethodVisitor(methodVisitor);
        }

        return methodVisitor;
    }

}
