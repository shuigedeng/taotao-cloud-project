package com.taotao.cloud.lock.kylin.service.impl;

import com.taotao.cloud.lock.kylin.model.User;
import com.taotao.cloud.lock.kylin.service.IndexService;
import com.wjk.kylin.lock.annotation.KylinLock;
import com.wjk.kylin.lock.enums.LockType;
import com.wjk.kylin.lock.executor.redisson.RedissonLockExecutor;
import com.wjk.kylin.lock.executor.zookeeper.ZookeeperLockExecutor;
import com.wjk.kylin.lock.model.LockInfo;
import com.wjk.kylin.lock.template.LockTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private LockTemplate lockTemplate;

	@KylinLock(executor = ZookeeperLockExecutor.class)
	@Override
	public void simple1() {
		System.out.println("执行简单方法1 , 当前线程:" + Thread.currentThread().getName());
	}


	@KylinLock(keys = "#lockKey")
	@Override
	public void simple2(String lockKey) {
		System.out.println("执行简单方法2 , 当前线程:" + Thread.currentThread().getName());
	}

	@Override
	@KylinLock(keys = "#user.id", acquireTimeout = 15000, expire = 1000, executor = RedissonLockExecutor.class)
	public User method1(User user) {
		System.out.println("执行method1 , 当前线程:" + Thread.currentThread().getName());
		//模拟锁占用,占用时间超过 expire 时间，自动释放锁
		try {
			int count = 0;
			do {
				Thread.sleep(1000);
				System.out.println(
					"执行method1 , 当前线程:" + Thread.currentThread().getName() + " , 休眠秒："
						+ (count++));
			} while (count < 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	@KylinLock(keys = {"#user.id", "#user.name"}, acquireTimeout = 5000, expire = 5000)
	public User method2(User user) {
		System.out.println("执行method2 , 当前线程:" + Thread.currentThread().getName());
		//模拟锁占用
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void method3(String userId) {
		// 各种查询操作 不上锁
		// ...
		// 获取锁
		final LockInfo lockInfo = lockTemplate.lock(userId, 30000L, 5000L,
			RedissonLockExecutor.class);
		if (null == lockInfo) {
			throw new RuntimeException("业务处理中,请稍后再试");
		}
		// 获取锁成功，处理业务
		try {
			System.out.println("执行method3 , 当前线程:" + Thread.currentThread().getName());
		} finally {
			//释放锁
			lockTemplate.releaseLock(lockInfo);
		}
		//结束
	}

	@Override
	@KylinLock(keys = "1", expire = 60000)
	public void reentrantMethod1() {
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("reentrantMethod1" + getClass());
	}

	@Override
	@KylinLock(keys = "1")
	public void reentrantMethod2() {
		System.out.println("reentrantMethod2" + getClass());
	}

	/**
	 * redisson expire = -1 则续期
	 *
	 * @param key
	 */
	@Override
	@KylinLock(expire = -1, keys = "#key")
	public void simple3(String key) {
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("执行simple3 , 当前线程:" + Thread.currentThread().getName());
	}

	/**
	 * 加入便捷的编程式上锁工具（不需要用户手动释放锁）
	 *
	 * @param key
	 */
	@Override
	public void execute1(String key) {
		Integer num = lockTemplate.execute(key, IndexServiceImpl::getNumber);
		System.out.println(
			"执行execute1 , 当前线程:" + Thread.currentThread().getName() + " , 获取的返回值是："
				+ num);
	}

	@Override
	@KylinLock(name = "read-write", acquireTimeout = 0, lockType = LockType.READ)
	public void read1(String key) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(
			"执行方法read1 , 当前线程:" + Thread.currentThread().getName() + "threadId:"
				+ Thread.currentThread().getId());
	}

	@Override
	@KylinLock(name = "read-write", acquireTimeout = 0, lockType = LockType.WRITE)
	public void write1(String key) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("执行方法write1 , 当前线程:" + Thread.currentThread().getName());
	}

	@Override
	@KylinLock(name = "reentrant_key", expire = 60000)
	public void demoMethod2() {
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod2" + getClass());
	}

	@Override
	@KylinLock(name = "semaphore_key", lockType = LockType.SEMAPHORE, executor = ZookeeperLockExecutor.class)
	public void demoMethod9() {
		System.out.println("demoMethod9" + getClass());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod9" + getClass());
	}

	@Override
	@KylinLock(name = "fair_lock_key", lockType = LockType.FAIR, executor = RedissonLockExecutor.class)
	public void demoMethod8() {
		System.out.println("demoMethod8" + getClass());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod8" + getClass());
	}

	@Override
	@KylinLock(name = "reentrant_lock_key", acquireTimeout = 0, lockType = LockType.REENTRANT, executor = ZookeeperLockExecutor.class)
	public void demoMethod15() {
		System.out.println("demoMethod15" + getClass());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("demoMethod15" + getClass());
	}


	public static Integer getNumber() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("执行getNumber方法 , 当前线程:" + Thread.currentThread().getName());

		return 1;
	}
}
