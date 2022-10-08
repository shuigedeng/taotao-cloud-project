package com.taotao.cloud.lock.kylin.service;

import com.taotao.cloud.lock.kylin.custom.DemoLockFailureCallBack;
import com.taotao.cloud.lock.kylin.model.User;
import com.wjk.kylin.lock.annotation.KylinLock;
import com.wjk.kylin.lock.enums.LockType;
import com.wjk.kylin.lock.executor.redisson.RedissonLockExecutor;
import com.wjk.kylin.lock.executor.zookeeper.ZookeeperLockExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl {

	@Autowired
	private IndexService indexService;

	@KylinLock(keys = "1", expire = 60000)
	public void demoMethod1() {
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod1" + getClass());
	}

	@KylinLock(name = "reentrant_key", expire = 60000)
	public void demoMethod2() {
		System.out.println("demoMethod2 - start" + getClass());

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//重入锁
		indexService.demoMethod2();

		System.out.println("demoMethod2 - end " + getClass());
	}

	//重复注解 加多把锁
	@KylinLock(name = "reentrant_key1", expire = 60000)
	@KylinLock(name = "reentrant_key2", expire = 60000)
	@KylinLock(name = "reentrant_key3", expire = 60000)
	public void demoMethod3() {
		System.out.println("demoMethod3 - start" + getClass());

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod3 - end " + getClass());
	}

	@KylinLock(name = "multi_lock_key", lockType = LockType.MULTI)
	public void demoMethod4() {
		System.out.println("demoMethod4 - start" + getClass());

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod4 - end " + getClass());
	}

	/**
	 * <p>
	 * keys 如果没匹配到 #{a}没有解析出来则是 kylin-lock:reentrant_key3 支持数字、'a'、运算符 如果不需要匹配方法参数，则需要用单引号
	 * kylin-lock:reentrant_key3#3
	 */
	@KylinLock(name = "reentrant_key1", keys = "'a'", expire = 60000, executor = RedissonLockExecutor.class)
	@KylinLock(name = "reentrant_key2", keys = "'a_b'", expire = 60000, executor = ZookeeperLockExecutor.class)
	@KylinLock(name = "reentrant_key3", keys = "2+1", expire = 60000, executor = RedissonLockExecutor.class)
	public void demoMethod5() {
		System.out.println("demoMethod5 - start" + getClass());

		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod5 - end " + getClass());
	}

	//kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod6:a
	//kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod6:b
	///curator/kylin/lock/kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod6/b/_c_00a1d27c-afec-4205-ac64-a6f8e8328045-lock-0000000000
	///curator/kylin/lock/kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod6/a/_c_a70a51bd-0e80-4d1c-ae75-bb584c637b19-lock-0000000000
	@KylinLock(lockType = LockType.MULTI)
	public void demoMethod6() {
		System.out.println("demoMethod6 - start" + getClass());

		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod6 - end " + getClass());
	}

	//kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod7:red1
	//kylin-lock:com.wjk.kylin.lock.samples.service.DemoServiceImpl.demoMethod7:red2
	@KylinLock(lockType = LockType.RED, keySuffix = {"'red1'",
		"'red2'"}, executor = RedissonLockExecutor.class)
	public void demoMethod7() {
		System.out.println("demoMethod7 - start" + getClass());

		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod7 - end " + getClass());
	}

	/**
	 * 公平锁
	 */
	@KylinLock(name = "fair_lock_key", lockType = LockType.FAIR, executor = RedissonLockExecutor.class)
	public void demoMethod8() {
		System.out.println("demoMethod8 - start" + getClass());

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		indexService.demoMethod8();

		System.out.println("demoMethod8 - end " + getClass());
	}

	/**
	 * zk 不可重入锁
	 */
	@KylinLock(name = "semaphore_key", acquireTimeout = 0, lockType = LockType.SEMAPHORE, executor = ZookeeperLockExecutor.class)
	public void demoMethod9() {
		System.out.println("demoMethod9 - start" + getClass());
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		indexService.demoMethod9();
		System.out.println("demoMethod9 - end " + getClass());
	}

	/**
	 * 红锁
	 *
	 * @param user
	 */
	@KylinLock(lockType = LockType.RED, keySuffix = {"#user.id",
		"'red2'"}, executor = RedissonLockExecutor.class)
	public void demoMethod11(User user) {
		System.out.println("demoMethod11 - start" + getClass());

		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("demoMethod11 - id:" + user.getId() + ",name:" + user.getName());
	}

	@KylinLock(acquireTimeout = 0, lockFailure = DemoLockFailureCallBack.class)
	public void demoMethod12(String name) {
		System.out.println("demoMethod12 - start, name：" + name);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod12 - end, name：" + name);
	}

	@KylinLock(acquireTimeout = 0, lockFailure = DemoLockFailureCallBack.class)
	public void demoMethod13() {
		System.out.println("demoMethod12 - start");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod12 - end");
	}

	@KylinLock(acquireTimeout = 0, lockFailure = DemoLockFailureCallBack.class)
	public Integer demoMethod14(Integer num) {
		System.out.println("demoMethod12 - start,num:" + num);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod12 - end,num:" + num);

		return num * 2;
	}

	@KylinLock(name = "reentrant_lock_key", acquireTimeout = 0, lockType = LockType.REENTRANT, executor = ZookeeperLockExecutor.class)
	public void demoMethod15() {
		System.out.println("demoMethod15 - start" + getClass());
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		indexService.demoMethod15();
		System.out.println("demoMethod15 - end " + getClass());
	}
}
