/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.application.statemachine.squirrel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.squirrelframework.foundation.fsm.*;

import java.util.HashMap;
import java.util.Map;

/** StateMachineBuilder实例 */
public class StateMachineEngine<T extends UntypedStateMachine, S, E, C> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static Map<String, UntypedStateMachineBuilder> builderMap =
            new HashMap<String, UntypedStateMachineBuilder>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Transactional
    public void fire(Class<T> machine, S state, E event, C context) {
        StateMachineBuilder stateMachineBuilder = this.getStateMachineBuilder(machine);
        StateMachine stateMachine = stateMachineBuilder.newStateMachine(state, applicationContext);
        stateMachine.fire(event, context);
    }

    private StateMachineBuilder getStateMachineBuilder(Class<T> stateMachine) {
        UntypedStateMachineBuilder stateMachineBuilder = builderMap.get(stateMachine.getName());
        if (stateMachineBuilder == null) {
            stateMachineBuilder = StateMachineBuilderFactory.create(stateMachine, ApplicationContext.class);
            builderMap.put(stateMachine.getName(), stateMachineBuilder);
        }
        return stateMachineBuilder;
    }
}
