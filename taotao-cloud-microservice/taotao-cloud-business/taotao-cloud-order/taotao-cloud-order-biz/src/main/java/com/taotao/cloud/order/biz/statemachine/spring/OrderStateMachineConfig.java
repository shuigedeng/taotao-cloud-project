package com.taotao.cloud.order.biz.statemachine.spring;

import java.util.EnumSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * OrderStateMachineConfig
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Configuration
@EnableStateMachine(name = "OrderStateMachine")
@Slf4j
public class OrderStateMachineConfig extends
        EnumStateMachineConfigurerAdapter<OrderStatusEnum, OrderEvent> {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 订单状态由审核通过 -> 放款成功 触发条件：订单申请金额=放款金额
     */
    @Bean
    public Guard<OrderStatusEnum, OrderEvent> guardForLoanSuccessByLoan() {
        return context -> {
            // 从扩展信息中获取参数
            StateMachineParam param = (StateMachineParam) context.getExtendedState().getVariables()
                    .get("param");
            BizOrder order = param.getBizOrder();
            // 如果申请金额=放款金额 ，返回true，状态机就会流转到调用此方法的目标状态
            if (order.getApplyAmount().compareTo(order.getLoanAmlunt) == 0) {
                return true;
            }
            return false;
        };
    }

    /**
     * 订单状态由审核通过 -> 部分放款成功 触发条件：订单申请金额<放款金额
     */
    @Bean
    public Guard<OrderStatusEnum, OrderEvent> guardForPartiallyLoanSuccessByLoan() {
        return context -> {
            // 从扩展信息中获取参数
            StateMachineParam param = (StateMachineParam) context.getExtendedState().getVariables()
                    .get("param");
            BizOrder order = param.getBizOrder();
            // 如果申请金额<放款金额 ，返回true，状态机就会流转到调用此方法的目标状态
            if (order.getApplyAmount().compareTo(order.getLoanAmlunt) < 0) {
                return true;
            }
            return false;
        };
    }


    /**
     * 配置状态
     */
    @Override
    public void configure( StateMachineStateConfigurer<OrderStatusEnum, OrderEvent> states )
            throws Exception {
        states.withStates()
                .initial(OrderStatusEnum.APPROVE_PENDING) // 设置初始状态为[待审核]
                .states(EnumSet.allOf(OrderStatusEnum.class));
    }


    /**
     * 配置状态转换事件关系
     */
    @Override
    public void configure( StateMachineTransitionConfigurer<OrderStatusEnum, OrderEvent> transitions )
            throws Exception {
        transitions
                //当执行 【开始审核】操作时，将订单状态由待审核 -> 审核中
                .withExternal().source(OrderStatusEnum.APPROVE_PENDING)
                .target(OrderStatusEnum.APPROVE_ING).event(OrderEvent.APPROVE_START)
                .and()
                //当执行 【审核失败】操作时，将订单状态由审核中 -> 审核失败
                .withExternal().source(OrderStatusEnum.APPROVE_ING)
                .target(OrderStatusEnum.APPROVE_FAILED).event(OrderEvent.APPROVE_FAILED)
                .and()
                //当执行 【审核成功】操作时，将订单状态由审核中 -> 审核成功
                .withExternal().source(OrderStatusEnum.APPROVE_ING)
                .target(OrderStatusEnum.APPROVE_SUCCESS).event(OrderEvent.APPROVE_SUCCESS)
                .and()
                //当执行 【放款】操作时，将订单状态由审核成功 -> 放款成功
                .withExternal().source(OrderStatusEnum.APPROVE_SUCCESS)
                .target(OrderStatusEnum.LOAN_SUCCESS).event(OrderEvent.LOAN)
                .guard(guardForLoanSuccessByLoan())
                .and()
                //当执行 【放款】操作时，将订单状态由审核成功 -> 部分放款成功
                .withExternal().source(OrderStatusEnum.APPROVE_SUCCESS)
                .target(OrderStatusEnum.PARTIALLY_LOAN_SUCCESS).event(OrderEvent.LOAN)
                .guard(guardForPartiallyLoanSuccessByLoan());
    }


    /**
     * 持久化配置
     */
    @Bean
    public DefaultStateMachinePersister persister() {
        return new DefaultStateMachinePersister<>(
                new StateMachinePersist<OrderStatusEnum, OrderEvent, BizOrder>() {
                    @Override
                    public void write( StateMachineContext<OrderStatusEnum, OrderEvent> context,
                            BizOrder order ) throws Exception {
                        OrderStatusEnum orderStatus = context.getState();
                        log.info("订单状态持久化,订单ID：{},目标状态:{}", order.getId(), orderStatus);
                        orderMapper.updateOrderStatus(order.getId(), orderStatus);
                    }

                    @Override
                    public StateMachineContext<OrderStatusEnum, OrderEvent> read( BizOrder order )
                            throws Exception {
                        log.info("恢复订单状态机状态");
                        return new DefaultStateMachineContext<>(order.getStatus(), null, null, null);
                    }

                });
    }


}
