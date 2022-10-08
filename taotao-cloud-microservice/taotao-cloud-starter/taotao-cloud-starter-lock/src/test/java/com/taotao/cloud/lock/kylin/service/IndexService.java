package com.taotao.cloud.lock.kylin.service;


import com.taotao.cloud.lock.kylin.model.User;

public interface IndexService {

	void simple1();

	void simple2(String lockKey);

	User method1(User user);

	User method2(User user);

	void method3(String userId);

	void reentrantMethod1();

	void reentrantMethod2();

	void simple3(String key);

	void execute1(String key);

	void read1(String key);

	void write1(String key);

	void demoMethod2();

	void demoMethod9();

	void demoMethod8();

	void demoMethod15();
}
