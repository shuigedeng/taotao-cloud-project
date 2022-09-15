package com.taotao.cloud.sys.biz.event.transactional;


import org.springframework.context.ApplicationEvent;

/**
 * <pre class="code">
 * &#064;Service
 * &#064;Slf4j
 * public class UserService implements ApplicationEventPublisherAware {
 *
 *     private ApplicationEventPublisher eventPublisher;
 *
 *     &#064;Autowired
 *     private JdbcTemplate jdbcTemplate;
 *
 *     &#064;Transactional
 *     public void demo() {
 *
 *         User user = new User("zhangsan", "M", 30);
 *
 *         // 发布事件，等事务commit之后执行
 *         eventPublisher.publishEvent(new DemoEvent(user));
 *
 *         jdbcTemplate.update(
 *                 "insert into t_user (`name`, `sex`, `age`) values (?, ?, ?)",
 *                 user.getName(),
 *                 user.getSex(),
 *                 user.getAge());
 *         log.info("事务中的业务逻辑执行完毕");
 *     }
 *
 *     &#064;Override
 *     public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
 *         this.eventPublisher = applicationEventPublisher;
 *     }
 * }
 * </pre>
 */
public class DemoEvent extends ApplicationEvent {

	public DemoEvent(Object source) {
		super(source);
	}
}
