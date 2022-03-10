package com.taotao.cloud.demo.compiler;

import com.taotao.cloud.common.utils.compiler.ByteCodeLoader;
import org.junit.Assert;
import org.junit.Test;

/**
 * compiler test
 *
 */
public class CompilerTest {

	@Test
	public void test() throws ClassNotFoundException {
		String className = "Foo";
		String sourceCode = "public class " + className + " {" +
			"    public void bar() {" +
			"        System.out.println(\"Hello from bar !\");" +
			"    }" +
			"}";
		Class<?> clazz = ByteCodeLoader.load(className, sourceCode);
		Assert.assertEquals(className, clazz.getName());
	}

}
