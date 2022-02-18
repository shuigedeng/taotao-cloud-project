package com.taotao.cloud.sys.biz.config;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.redis.delay.MessageConversionException;
import com.taotao.cloud.redis.delay.annotation.RedissonListener;
import com.taotao.cloud.redis.delay.config.RedissonQueue;
import com.taotao.cloud.redis.delay.message.DefaultRedissonMessageConverter;
import com.taotao.cloud.redis.delay.message.MessageConverter;
import com.taotao.cloud.redis.delay.message.QueueMessage;
import com.taotao.cloud.redis.delay.message.QueueMessageBuilder;
import com.taotao.cloud.redis.delay.message.RedissonHeaders;
import com.taotao.cloud.redis.delay.message.RedissonMessage;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class RedissonTestApplication {

	@Bean
	public RedissonQueue redissonQueue() {
		return new RedissonQueue("riven", true, null, new DefaultRedissonMessageConverter());
	}

    @Bean("myMessageConverter")
    public MessageConverter messageConverter() {
        return new MessageConverter() {
            @Override
            public QueueMessage<?> toMessage(Object object, Map<String, Object> headers) throws MessageConversionException {
                //do something you want, eg:
                headers.put("my_header", "my_header_value");
                return QueueMessageBuilder.withPayload(object).headers(headers).build();
            }

            @Override
            public Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
                String payload = redissonMessage.getPayload();
                String payloadStr = new String(payload);
                return JSONObject.parseObject(payloadStr, CarLbsDto.class);
            }
        };
    }

    @RedissonListener(queues = "riven", messageConverter = "myMessageConverter")
    public void handler(@Header(value = RedissonHeaders.MESSAGE_ID, required = false) String messageId,
                        @Header(RedissonHeaders.DELIVERY_QUEUE_NAME) String queue,
                        @Header(RedissonHeaders.SEND_TIMESTAMP) long sendTimestamp,
                        @Header(RedissonHeaders.EXPECTED_DELAY_MILLIS) long expectedDelayMillis,
                        @Header(value = "my_header", required = false, defaultValue = "test") String myHeader,
                        @Payload CarLbsDto carLbsDto) {
        System.out.println(messageId);
        System.out.println(queue);
        System.out.println(myHeader);
        long actualDelay = System.currentTimeMillis() - (sendTimestamp + expectedDelayMillis);
        System.out.println("receive " + carLbsDto + ", delayed " + actualDelay + " millis");
    }

	public static class CarLbsDto{
		private String cid;
		private String businessType;
		private String city;
		private String cityId;
		private String name;
		private String carNum;

		public String getCid() {
			return cid;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCarNum() {
			return carNum;
		}

		public void setCarNum(String carNum) {
			this.carNum = carNum;
		}
	}
}
