package com.taotao.cloud.sys.biz.event.transactional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;
import com.taotao.cloud.common.utils.log.LogUtils;
/**
 * {@snippet:
 *        @Autowired
 * private ApplicationEventPublisher applicationEventPublisher;
 *
 * // 执行任务
 * applicationEventPublisher.publishEvent(new TaskEvent(() -> {
 *     executeTask(param.getMainTaskId()); // 业务异步方法
 * }));
 *
 *
 *  //事务提交后执行
 *         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
 *             @Override
 *             public void afterCommit() {
 *
 *                 LogUtils.info("事务提交后执行 ===== ");
 *
 *             }
 *         });
 *
 *
 *         当B操作有数据改动并持久化时，并希望在A操作的AFTER_COMMIT阶段执行，
 *         那么你需要将B事务声明为PROPAGATION_REQUIRES_NEW。
 *         这是因为A操作的事务提交后，事务资源可能仍然处于激活状态，
 *         如果B操作使用默认的PROPAGATION_REQUIRED的话，会直接加入到操作A的事务中，
 *         但是这时候事务A是不会再提交，结果就是程序写了修改和保存逻辑，
 *         但是数据库数据却没有发生变化，解决方案就是要明确的将操作B的事务设为PROPAGATION_REQUIRES_NEW。
 *
 *
 *
 *         @Component
 * @Slf4j
 * public class TransactionalListener {
 *
 *        @Autowired
 *     private ProjectService projectService;
 *
 *     @TransactionalEventListener
 *     public void projectInit(ProjectInitEvent event) throws GcpPlatformException {
 *         //处理对应的事件逻辑，比如这里是 项目初始化的监听，处理项目初始化的逻辑即可
 *         projectService.init(event.getProjectCode());
 *     }
 * }
 * }
 */
@Component
public class TaskListener {

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
		classes=TaskEvent.class,
		fallbackExecution = true)
    public void taskHandler(TaskEvent taskEvent) {
        LogUtils.info("=============> start taskHandler：" + Thread.currentThread().threadId() + ", name : " + Thread.currentThread().getName());
        if (Objects.nonNull(taskEvent)) {
            taskEvent.getSource().run();
        }

    }
}
