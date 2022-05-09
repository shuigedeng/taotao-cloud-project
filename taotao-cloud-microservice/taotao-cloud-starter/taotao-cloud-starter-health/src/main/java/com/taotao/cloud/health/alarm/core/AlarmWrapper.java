package com.taotao.cloud.health.alarm.core;


import com.taotao.cloud.health.alarm.common.concurrent.DefaultThreadFactory;
import com.taotao.cloud.health.alarm.core.helper.ExecuteHelper;
import com.taotao.cloud.health.alarm.core.loader.ConfLoaderFactory;
import com.taotao.cloud.health.alarm.core.loader.api.IConfLoader;
import com.taotao.cloud.health.alarm.core.util.IpUtil;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmWrapper {
    private static final Logger logger = LoggerFactory.getLogger(AlarmWrapper.class);


    private ExecutorService alarmExecutorService;


    private ConcurrentHashMap<String, AtomicInteger> alarmCountMap;


    private IConfLoader confLoader;


    public static AlarmWrapper getInstance() {
        return InnerInstance.instance;
    }


    private static class InnerInstance {
        static AlarmWrapper instance = new AlarmWrapper();
    }


    private AlarmWrapper() {
        // 记录每种异常的报警数
        alarmCountMap = new ConcurrentHashMap<>();


        // 加载报警配置信息
        confLoader = ConfLoaderFactory.loader();

        // 初始化线程池
        initExecutorService();
    }


    public void initExecutorService() {
        // 报警线程池
        alarmExecutorService = new ThreadPoolExecutor(3, 5, 60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10), new DefaultThreadFactory("sms-sender"),
                new ThreadPoolExecutor.CallerRunsPolicy());


        // 每分钟清零一把报警计数
        ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(1);
        scheduleExecutorService.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, AtomicInteger> entry : alarmCountMap.entrySet()) {
                entry.getValue().set(0);
            }
        }, 0, 1, TimeUnit.MINUTES);
    }


    /**
     * 报警接口
     *
     * @param key     报警类型
     * @param content 报警内容
     */
    public void sendMsg(String key, String content) {
        sendMsg(new AlarmContent(key, null, content, null, AlarmContent.DEFAULT_CONTENT_TEMPLATE));
    }


    /**
     * 报警接口
     *
     * @param key     报警类型
     * @param title   报警标题, 如果为null，则会用默认的title来替换
     * @param content 报警内容
     */
    public void sendMsg(String key, String title, String content) {
        sendMsg(new AlarmContent(key, title, content, null, AlarmContent.DEFAULT_CONTENT_TEMPLATE));
    }


    /**
     * 报警接口
     *
     * @param key      报警类型
     * @param title    报警标题, 如果为null，则会用默认的title来替换
     * @param content  报警内容
     * @param template 报警模板，如果为null，表示选择默认的模板
     *                 形如  " ip:{0} >>> key:{1} >>> 异常数:{2} >>> {3}"
     *                 其中 0 表示会用当前机器的ip替换
     *                 1 表示用报警类型 key 替换
     *                 2 表示用当前的报警计数替换
     *                 3 表示用报警内容 content 替换
     *                 实例：
     *                 - content 为 测试报警内容
     *                 - 默认的一个报警内容为：  ip:127.0.0.1 >>> key:testAlarm >>> 异常数:10 >>> 测试报警内容
     *                 - 若模板为: "{3}"， 则表示报警内容，就是content内容
     *                 - 若模板为："{3}, 报警频率: {2}", 则报警内容为： 测试报警内容, 报警频率: 10
     */
    public void sendMsg(String key, String title, String content, String template) {
        sendMsg(new AlarmContent(key, title, content, null, template));
    }


    /**
     * 报警接口
     *
     * @param key     报警类型
     * @param content 报警内容
     * @param users   报警用户，如果指定，则以传入的用户为准；否则，报警用户为报警规则中指定的用户
     */
    public void sendMsgToUser(String key, String content, List<String> users) {
        sendMsg(new AlarmContent(key, null, content, users, AlarmContent.DEFAULT_CONTENT_TEMPLATE));
    }


    /**
     * 报警接口
     *
     * @param key     报警类型
     * @param title   报警标题, 如果为null，则会用默认的title来替换
     * @param content 报警内容
     * @param users   报警用户，如果指定，则以传入的用户为准；否则，报警用户为报警规则中指定的用户
     */
    public void sendMsgToUser(String key, String title, String content, List<String> users) {
        sendMsg(new AlarmContent(key, title, content, users, AlarmContent.DEFAULT_CONTENT_TEMPLATE));
    }


    /**
     * 报警接口
     *
     * @param key      报警类型
     * @param title    报警标题, 如果为null，则会用默认的title来替换
     * @param content  报警内容
     * @param template 报警模板，如果为null，表示选择默认的模板
     *                 形如  " ip:{0} >>> key:{1} >>> 异常数:{2} >>> {3}"
     *                 其中 0 表示会用当前机器的ip替换
     *                 1 表示用报警类型 key 替换
     *                 2 表示用当前的报警计数替换
     *                 3 表示用报警内容 content 替换
     *                 实例：
     *                 - content 为 测试报警内容
     *                 - 默认的一个报警内容为：  ip:127.0.0.1 >>> key:testAlarm >>> 异常数:10 >>> 测试报警内容
     *                 - 若模板为: "{3}"， 则表示报警内容，就是content内容
     *                 - 若模板为："{3}, 报警频率: {2}", 则报警内容为： 测试报警内容, 报警频率: 10
     * @param users    报警用户，如果指定，则以传入的用户为准；否则，报警用户为报警规则中指定的用户
     */
    public void sendMsgToUser(String key, String title, String content, String template, List<String> users) {
        sendMsg(new AlarmContent(key, title, content, users, template));
    }


    /**
     * 1. 报警计数
     * 2. 获取报警执行器
     * 3. 执行报警
     *
     * @param alarmContent
     */
    private void sendMsg(AlarmContent alarmContent) {
        try {
            // get alarm count
            int count = getAlarmCount(alarmContent.key);
            alarmContent.setCount(count);


            // get alarm executor
            List<ExecuteHelper> executeHelper = confLoader.getExecuteHelper(alarmContent.key, count);


            // do send msg
            executeHelper.forEach(executeHelper1 -> doSend(executeHelper1, alarmContent));
        } catch (Exception e) {
            logger.error("AlarmWrapper.sendMsg error! content:{}, e:{}", alarmContent, e);
        }
    }


    private void doSend(final ExecuteHelper executeHelper, final AlarmContent alarmContent) {
        alarmExecutorService.execute(() -> executeHelper.getIExecute().
                sendMsg(
                        // 如果显示指定了报警用户，则以指定的为准；否则选择报警规则的报警用户
                        Optional.ofNullable(alarmContent.getAlarmUser()).orElse(executeHelper.getUsers()),
                        alarmContent.getTitle(),
                        alarmContent.getContent()));
    }


    /**
     * 线程安全的获取报警总数 并自动加1
     *
     * @param key
     * @return
     */
    private int getAlarmCount(String key) {
        if (!alarmCountMap.containsKey(key)) {
            synchronized (this) {
                if (!alarmCountMap.containsKey(key)) {
                    alarmCountMap.put(key, new AtomicInteger(0));
                }
            }
        }

        return alarmCountMap.get(key).addAndGet(1);
    }


    /**
     * 报警的实体类
     */
    private static class AlarmContent {
        static final String DEFAULT_CONTENT_TEMPLATE = " ip:{0} >>> key:{1} >>> 异常数:{2} >>> {3}";

        private static String LOCAL_IP;

        private static String PREFIX;

        static {
            LOCAL_IP = IpUtil.getLocalIp();

            try {
                PREFIX = "[" + ConfLoaderFactory.loader().getRegisterInfo().getAppName() + "]";
            } catch (Exception e) {
                PREFIX = "[报警]";
            }
        }


        private String key;
        private String title;
        private String content;
        private int count;
        private List<String> alarmUser;
        private String template;


        public AlarmContent(String key, String title, String content, List<String> alarmUser, String template) {
            this.key = key;
            this.title = title;
            this.content = content;
            this.alarmUser = alarmUser;
            this.template = Optional.ofNullable(template).orElse(DEFAULT_CONTENT_TEMPLATE);
        }

        public String getTitle() {
            if (title == null) {
                return PREFIX;
            } else {
                return title;
            }
        }


        public void setCount(int count) {
            this.count = count;
        }


        public String getContent() {
            return MessageFormat.format(template, LOCAL_IP, key, count, content);
        }


        public List<String> getAlarmUser() {
            if (alarmUser == null || alarmUser.isEmpty()) {
                return null;
            }

            return alarmUser;
        }

	    @Override
	    public String toString() {
		    return "AlarmContent{" +
			    "key='" + key + '\'' +
			    ", title='" + title + '\'' +
			    ", content='" + content + '\'' +
			    ", count=" + count +
			    ", alarmUser=" + alarmUser +
			    ", template='" + template + '\'' +
			    '}';
	    }
    }
}
