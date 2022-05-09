package com.taotao.cloud.health.alarm.plugin.email;

import com.taotao.cloud.health.alarm.core.execut.api.IExecute;
import com.taotao.cloud.health.alarm.plugin.email.wrapper.EmailWrapper;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailExecute implements IExecute {

    private static final Logger logger = LoggerFactory.getLogger("alarm");


    @Override
    public void sendMsg(List<String> users, String title, String msg) {
        try {
            HtmlEmail email = EmailWrapper.genEmailClient();
            email.setSubject(title);
            email.setHtmlMsg(msg);
            for (String u : users) {
                email.addTo(u);
            }

            email.send();
        } catch (EmailException e) {
            logger.error("email send error! users: {}, title: {}, msg: {} e:{}", users, title, msg, e);
        }
    }


}
