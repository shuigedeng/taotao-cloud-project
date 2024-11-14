package com.taotao.cloud.ai.springai.constants;

/**
 * 系统提问AI常量
 *
 */
public interface ChatRequestConstant {

    String SYSTEM_SIMPLIFICATION = "简要总结一下对话内容，用作后续的上下文提示 prompt，控制在 200 字以内";

    String SYSTEM_HISTORICAL_MESSAGES = "这是历史聊天总结作为前情提要: %s";

}
