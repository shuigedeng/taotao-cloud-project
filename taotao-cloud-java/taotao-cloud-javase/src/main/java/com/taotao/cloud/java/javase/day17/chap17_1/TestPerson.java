package com.taotao.cloud.java.javase.day17.chap17_1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


//-verbose:class 显示类的加载过程
public class TestPerson {
	public static void main(String[] args) throws Exception {
//		Person zhangsan=new Person();
//		zhangsan.name="张三";
//		zhangsan.eat();
		//getClazz();
//		reflectOpe1();
		//reflectOpe2();
		//reflectOpe3();
		
//		Properties properties=new Properties();
//		properties.setProperty("name","zhangsan");
//		System.out.println(properties.toString());
//		invokeAny(properties, "setProperty", new Class[] {String.class, String.class}, "username","张三");
//		System.out.println(properties.toString());
		
		reflectOpe4();
	}
	//获取类对象的三种方式
	public static void getClazz() throws Exception {
		//1使用对象获取类对象
		Person zhangsan=new Person();
		Class<?> class1=zhangsan.getClass();
		System.out.println(class1.hashCode());
		//2使用类名.class属性
		Class<?> class2=Person.class;
		System.out.println(class2.hashCode());
		//3使用Class的静态方法[推荐使用]
		Class<?> class3=Class.forName("com.qf.chap17_1.Person");
		System.out.println(class3.hashCode());
	}
	
	//1 使用反射获取类的名字、包名、父类、接口
	public static void reflectOpe1() throws Exception {
		//(1)获取类对象 Person
		Class<?> class1=Class.forName("com.qf.chap17_1.Person");
		//getName();
		System.out.println(class1.getName());
		//getPackage();
		System.out.println(class1.getPackage().getName());
		//getSuperClass();
		System.out.println(class1.getSuperclass().getName());
		//getInterfaces();
		Class<?>[] classes=class1.getInterfaces();
		System.out.println(Arrays.toString(classes));
		
		System.out.println(class1.getSimpleName());
		System.out.println(class1.getTypeName());
		
	}
	
	//2使用反射获取类的构造方法，创建对象
	public static void reflectOpe2() throws Exception{
		//(1)获取类的类对象
		Class<?> class1=Class.forName("com.qf.chap17_1.Person");
		//(2)获取类的构造方法 Constructor
//		Constructor<?>[] cons=class1.getConstructors();
//		for (Constructor<?> con : cons) {
//			System.out.println(con.toString());
//		}
		//(3)获取类中无参构造
		Constructor<?> con=class1.getConstructor();
		Person zhangsan=(Person)con.newInstance();
		Person lisi=(Person)con.newInstance();
		System.out.println(zhangsan.toString());
		System.out.println(lisi.toString());
		//简便方法:类对象.newInstance();
		Person wangwu=(Person)class1.newInstance();
		System.out.println(wangwu.toString());
		//(4)获取类中带参构造方法
		Constructor<?> con2=class1.getConstructor(String.class,int.class);
		Person xiaoli=(Person)con2.newInstance("晓丽",20);
		System.out.println(xiaoli.toString());
		
	}
	
	//3使用反射获取类中的方法，并调用方法
	public static void reflectOpe3() throws Exception{
		//（1）获取类对象
		Class<?> class1=Class.forName("com.qf.chap17_1.Person");
		//（2）获取方法  Method对象
		//2.1getMethods() 获取公开的方法，包括从父类继承的方法
		//Method[] methods=class1.getMethods();
		//2.2getDeclaredMethods() 获取类中的所有方法，包括私有、默认、保护的 、不包含继承的方法
//		Method[] methods=class1.getDeclaredMethods();
//		for (Method method : methods) {
//			System.out.println(method.toString());
//		}
		//（3）获取单个方法
		//3.1eat
		Method eatMethod=class1.getMethod("eat");
		//调用方法
		//正常调用方法  Person zhangsan=new Person();  zhangsan.eat();
		Person zhangsan=(Person)class1.newInstance();
		eatMethod.invoke(zhangsan);//zhangsan.eat();
		System.out.println("------------------");
		//3.2toString
		Method toStringMethod=class1.getMethod("toString");
		Object result=toStringMethod.invoke(zhangsan);
		System.out.println(result);
		System.out.println("-------------------");
		//3.3带参的eat 
		Method eatMethod2=class1.getMethod("eat", String.class);
		eatMethod2.invoke(zhangsan, "鸡腿");
		
		//3.4获取私有方法
		Method privateMethod=class1.getDeclaredMethod("privateMethod");
		//设置访问权限无效
		privateMethod.setAccessible(true);
		privateMethod.invoke(zhangsan);
		
		//3.4获取静态方法
		Method staticMethod=class1.getMethod("staticMethod");
		//正常调用 Person.staticMethod
		staticMethod.invoke(null);
		
	}
	
	//4使用反射实现一个可以调用任何对象方法的通用方法
	public static Object invokeAny(Object obj,String methodName,Class<?>[] types,Object...args) throws Exception {
		//1获取类对象
		Class<?> class1=obj.getClass();
		//2获取方法
		Method method=class1.getMethod(methodName, types);
		//3调用
		return method.invoke(obj, args);
	}
	
	//5使用反射获取类中的属性
	public static void reflectOpe4() throws Exception{
		//（1）获取类对象
		Class<?> class1=Class.forName("com.qf.chap17_1.Person");
		//（2）获取属性(字段) 公开的字段，父类继承的字段
		//Field[] fields=class1.getFields(); 
		//getDeclaredFields()获取所有的属性，包括私有，默认 ，包含，
//		Field[] fields=class1.getDeclaredFields();
//		System.out.println(fields.length);
//		for (Field field : fields) {
//			System.out.println(field.toString());
//		}
		//（3）获取name属性
		Field namefield=class1.getDeclaredField("name");
		namefield.setAccessible(true);
		//（4）赋值  正常调用  Person zhangsan=new Person(); zhangsan.name="张三";
		Person zhangsan=(Person)class1.newInstance();
		namefield.set(zhangsan, "张三"); //zhangsan.name="张三";
		//（5） 获取值
		System.out.println(namefield.get(zhangsan));// zhangsan.name
	}
}
