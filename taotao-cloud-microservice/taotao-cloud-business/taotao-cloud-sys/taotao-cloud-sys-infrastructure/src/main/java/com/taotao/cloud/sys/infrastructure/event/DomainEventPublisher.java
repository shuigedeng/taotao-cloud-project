

package com.taotao.cloud.sys.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 领域事件推送.
 */
@Component
@Slf4j
public class DomainEventPublisher {

	public void publish(ApplicationEvent event) {
	}

}
