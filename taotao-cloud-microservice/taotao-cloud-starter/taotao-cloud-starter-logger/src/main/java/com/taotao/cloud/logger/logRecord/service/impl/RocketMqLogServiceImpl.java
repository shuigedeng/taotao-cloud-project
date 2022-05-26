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
package com.taotao.cloud.logger.logRecord.service.impl;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.logger.logRecord.bean.LogDTO;
import com.taotao.cloud.logger.logRecord.configuration.LogRecordProperties;
import com.taotao.cloud.logger.logRecord.constants.LogConstants;
import com.taotao.cloud.logger.logRecord.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * 火箭mq日志服务实现类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:42:51
 */
@Service
@EnableConfigurationProperties({LogRecordProperties.class})
@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = LogConstants.DataPipeline.ROCKET_MQ)
public class RocketMqLogServiceImpl implements LogService {

	/**
	 * 属性
	 */
	@Autowired
    private LogRecordProperties properties;

	///**
	// * 默认mq生产商
	// */
	//@Autowired
    //private DefaultMQProducer defaultMqProducer;

	@Override
    public boolean createLog(LogDTO logDTO) {
        //try {
        //    Message msg = new Message(properties.getRocketMqProperties().getTopic(), properties.getRocketMqProperties().getTag(), (JSON.toJSONString(logDTO)).getBytes(RemotingHelper.DEFAULT_CHARSET));
        //    SendResult sendResult = defaultMqProducer.send(msg);
        //    LogUtil.info("LogRecord RocketMq send LogDTO [{}] sendResult: [{}]", logDTO, sendResult);
        //    return true;
        //} catch (Exception e) {
	    //    LogUtil.error("LogRecord RocketMq send LogDTO error", e);
        //    return false;
        //}
		return false;
    }
}
