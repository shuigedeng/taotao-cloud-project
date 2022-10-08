package com.taotao.cloud.lock.kylin;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.lock.kylin.model.User;
import com.taotao.cloud.lock.kylin.service.DemoServiceImpl;
import com.taotao.cloud.lock.kylin.service.IndexService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * https://blog.csdn.net/qq_44413835/article/details/117320028
 */

/**
 * <p>
 * https://gitee.com/baomidou/lock4j.git
 * </p>
 * zk是临时有序节点 InterProcessMutex 可重入公平锁，按照请求的顺序获取排它锁（从Zookeeper角度来看请求顺序) InterProcessSemaphoreMutex
 * 不可重入公平锁，按照请求的顺序获取排它锁（从Zookeeper角度来看请求顺序)
 */
@SpringBootTest
public class SpringBootLockTest {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IndexService indexService;

	@Autowired
	private DemoServiceImpl demoService;

	ExecutorService executorService = Executors.newFixedThreadPool(5);
	CountDownLatch countDownLatch = new CountDownLatch(100);

	@Test
	public void simple1Test() throws InterruptedException {
		Runnable task = () -> {
			try {
				indexService.simple1();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	@Test
	public void simpleTest2() throws InterruptedException {
		Runnable task = () -> {
			try {
				//kylin-lock:com.wjk.kylin.lock.example.service.impl.IndexServiceImpl.simple2#simple2_key
				indexService.simple2("simple2_key");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	@Test
	public void simpleTest3() throws InterruptedException {
		Runnable task = () -> {
			try {
				indexService.method1(new User(1L, "zs"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}


	@Test
	public void simpleTest4() throws InterruptedException {
		Runnable task = () -> {
			try {
				indexService.method2(new User(1L, "zs"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	/**
	 * 编程式锁
	 */

	@Test
	public void simpleTest5() throws InterruptedException {
		Runnable task = () -> {
			try {
				indexService.method3("admin");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	/**
	 * 续期
	 */

	@Test
	public void simpleTest6() throws InterruptedException {
		Runnable task = () -> {
			try {
				//kylin-lock:com.wjk.kylin.lock.example.service.impl.IndexServiceImpl.simple3#zs_key
				indexService.simple3("zs_key");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	/**
	 * 函数式调用
	 */

	@Test
	public void execute1() throws InterruptedException {
		Runnable task = () -> {
			try {
				indexService.execute1("execute1");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
		};
		for (int i = 0; i < 100; i++) {
			executorService.submit(task);
		}
		countDownLatch.await();
	}

	/**
	 * 读写锁 zookeeper 读锁也互斥？获取读锁有成功有失败
	 */

	@Test
	public void readWrite1() throws InterruptedException {
		Runnable read = () -> {
			try {
				indexService.read1("read-write");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		Runnable write = () -> {
			try {
				indexService.write1("read-write");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

//        executorService.submit(read);
//        executorService.submit(read);
//        executorService.submit(read);
//        executorService.submit(read);

//        executorService.submit(read);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executorService.submit(read);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executorService.submit(read);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executorService.submit(read);
//        executorService.submit(read);

//        executorService.submit(write);
//        executorService.submit(write);

		for (int i = 0; i < 20; i++) {
			executorService.submit(read);
		}
		for (int i = 0; i < 20; i++) {
			executorService.submit(write);
		}
		for (int i = 0; i < 20; i++) {
			executorService.submit(write);
			executorService.submit(read);
		}

		for (int i = 0; i < 20; i++) {
			executorService.submit(write);
		}
		for (int i = 0; i < 20; i++) {
			executorService.submit(read);
		}

		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * 非接口实现也生效
	 */

	@Test
	public void testDemoMethod1() {
		demoService.demoMethod1();
	}

	/**
	 * 重入锁
	 */

	@Test
	public void testDemoMethod2() {
		demoService.demoMethod2();
		demoService.demoMethod2();
		demoService.demoMethod2();
	}

	/**
	 * 重复注解
	 */

	@Test
	public void testDemoMethod3() {
		demoService.demoMethod3();
	}

	/**
	 * 联锁
	 */

	@Test
	public void testDemoMethod4() {
		demoService.demoMethod4();
	}

	/**
	 * 重复注解，多锁，可以不同的执行器
	 */

	@Test
	public void testDemoMethod5() {
		demoService.demoMethod5();
	}

	/**
	 * 联锁（本项目使用的同一redisson实例、不同的key） 自定义 锁个数以及 key后缀 所有的锁都上锁成功才算成功
	 */

	@Test
	public void testDemoMethod6() {
		demoService.demoMethod6();
	}

	/**
	 * 红锁（本项目使用的同一redisson实例、不同的key） 自定义 锁个数以及 key后缀 红锁采用主节点过半机制，即获取锁或者释放锁成功的标志为：在过半的节点上操作成功。n/2+1
	 */

	@Test
	public void testDemoMethod7() {
		demoService.demoMethod7();
	}

	/**
	 * redisson 公平锁
	 */

	@Test
	public void testDemoMethod8() {
		demoService.demoMethod8();
		demoService.demoMethod8();
	}

	/**
	 * zk 不可重入锁
	 */

	@Test
	public void testDemoMethod9() {
		demoService.demoMethod9();
	}

	/**
	 * 红锁联锁后缀key支持SpEL表达式
	 */

	@Test
	public void testDemoMethod11() {
		User user = new User(10L, "zs");
		demoService.demoMethod11(user);
	}

	/**
	 * 自定义失败回调 有参
	 */

	@Test
	public void testDemoMethod12() throws InterruptedException {
		Runnable lock = () -> {
			try {
				demoService.demoMethod12("zs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		for (int i = 0; i < 20; i++) {
			executorService.submit(lock);
		}
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * 自定义失败回调 无参
	 */

	@Test
	public void testDemoMethod13() throws InterruptedException {
		Runnable lock = () -> {
			try {
				demoService.demoMethod13();
			} catch (Exception e) {
				LogUtils.error("msg:", e);
			}
		};
		for (int i = 0; i < 2; i++) {
			executorService.submit(lock);
		}
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * 自定义失败回调 有参有返回
	 */

	@Test
	public void testDemoMethod14() throws InterruptedException {
		Runnable lock = () -> {
			try {
				Integer num = demoService.demoMethod14(18);
				LogUtils.info("testDemoMethod14 - get num:{}", num);
			} catch (Exception e) {
				LogUtils.error("msg:", e);
			}
		};
		for (int i = 0; i < 2; i++) {
			executorService.submit(lock);
		}
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * zk 重入锁
	 */

	@Test
	public void testDemoMethod15() {
		demoService.demoMethod15();
		demoService.demoMethod15();
	}

	/**
	 * 不能使用 lettuce jedis 连接池
	 * org.redisson.spring.starter.RedissonAutoConfiguration#redissonConnectionFactory(org.redisson.api.RedissonClient)
	 */

	@Test
	public void testDemoMethod16() {
		//共用一个连接池即是 redisson
		stringRedisTemplate.opsForValue().set("zs", "10", 100, TimeUnit.SECONDS);

		indexService.simple1();
	}

}
