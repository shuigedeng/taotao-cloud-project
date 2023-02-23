package com.taotao.cloud.message.biz.austin.handler.script;


import jakarta.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * sms发送脚本的抽象类
 *
 * @author 3y
 */
@Slf4j
public abstract class BaseSmsScript implements SmsScript {

	@Autowired
	private SmsScriptHolder smsScriptHolder;

	@PostConstruct
	public void registerProcessScript() {
		if (ArrayUtils.isEmpty(this.getClass().getAnnotations())) {
			log.error("BaseSmsScript can not find annotation!");
			return;
		}
		Annotation handlerAnnotations = null;
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof SmsScriptHandler) {
				handlerAnnotations = annotation;
				break;
			}
		}
		if (handlerAnnotations == null) {
			log.error("handler annotations not declared");
			return;
		}
		//注册handler
		smsScriptHolder.putHandler(((SmsScriptHandler) handlerAnnotations).value(), this);
	}
}
