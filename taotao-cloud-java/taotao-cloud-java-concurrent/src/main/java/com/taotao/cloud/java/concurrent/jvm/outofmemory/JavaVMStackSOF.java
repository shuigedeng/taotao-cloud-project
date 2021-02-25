package com.taotao.cloud.java.jvm.outofmemory;

/**
 * 虚拟机栈和本地方法栈OOM测试
 * VM Args：-Xss128k
 */
public class JavaVMStackSOF {
	private int stackLength = 1;

	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) throws Throwable {
		JavaVMStackSOF oom = new JavaVMStackSOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length：" + oom.stackLength);
			throw e;
		}
	}
}
