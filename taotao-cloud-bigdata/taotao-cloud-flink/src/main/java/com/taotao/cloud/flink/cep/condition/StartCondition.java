package com.taotao.cloud.flink.cep.condition;

import com.taotao.cloud.flink.cep.event.Event;
import org.apache.flink.cep.dynamic.condition.AviatorCondition;


public class StartCondition extends AviatorCondition<Event> {

    public StartCondition(String expression) {
        super(expression);
    }
}
