package com.taotao.cloud.ai.springai.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息对应角色枚举
 *
 */
@AllArgsConstructor
@Getter
public enum MessageRoleEnum{

    USER("user", "用户发送的消息"),
    /**
     * <a href="https://docs.spring.io/spring-ai/reference/api/chat/functions/openai-chat-functions.html">...</a>
     */
    FUNCTION("function", "自定义返回"),
    SYSTEM("system", "系统发送的消息"),
    ASSISTANT("assistant", "AI回复的消息");

    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    @JsonValue //  表示对枚举序列化时返回此字段
    private final String value;

    private final String label;
}
