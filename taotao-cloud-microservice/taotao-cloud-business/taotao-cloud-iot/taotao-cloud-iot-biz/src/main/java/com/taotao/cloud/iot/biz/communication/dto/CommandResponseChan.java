package com.taotao.cloud.iot.biz.communication.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 数据生产消费者通道
 */
@Slf4j
public class CommandResponseChan {

    // 存储通道的 ConcurrentHashMap
    private static final ConcurrentHashMap<String, CommandResponseChan> CHANNEL = new ConcurrentHashMap<>();

    private final CompletableFuture<BaseCommandResponseDTO> future = new CompletableFuture<>();

    private final Long DEFAULT_WAIT_MILLISECONDS = 5 * 1000L;

    // 私有构造函数，不允许外部直接实例化
    private CommandResponseChan() {
    }

    /**
     * 获取或创建通道实例
     *
     * @param commandId    通道标识
     * @param isNeedCreate 是否需要创建新的通道实例
     * @return 通道实例
     */
    public static CommandResponseChan getInstance(String commandId, boolean isNeedCreate) {
        if (!isNeedCreate) {
            return CHANNEL.get(commandId);
        }
        return CHANNEL.computeIfAbsent(commandId, k -> new CommandResponseChan());
    }

    /**
     * 从通道中获取数据，默认超时时间为 5 秒
     *
     * @param commandId 通道标识
     * @return 获取的数据，如果超时返回 null
     */
    public BaseCommandResponseDTO get(String commandId) {
        return get(commandId, DEFAULT_WAIT_MILLISECONDS);
    }

    /**
     * 从通道中获取数据，支持超时设置
     *
     * @param commandId 通道标识
     * @param timeout   超时时间（毫秒）
     * @return 获取的数据，如果超时返回 null
     */
    public BaseCommandResponseDTO get(String commandId, long timeout) {
        CommandResponseChan channel = CHANNEL.get(commandId);
        if (Objects.isNull(channel)) {
            return null;
        }
        try {
            return channel.future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // 超时异常处理
            log.error("Device response timeout. {}", commandId);
            return null;
        } catch (Exception e) {
            // 其他异常处理
            e.printStackTrace();
            return null;
        } finally {
            // 确保在获取数据后移除通道
            CHANNEL.remove(commandId, channel);
        }
    }

    /**
     * 向通道中放入数据，并唤醒可能正在等待数据的线程
     *
     * @param response 要放入的数据
     */
    public void put(BaseCommandResponseDTO response) {
        String commandId = response.getCommandId();
        if (commandId == null) {
            return;
        }
        CommandResponseChan channel = CHANNEL.get(commandId);
        if (Objects.isNull(channel)) {
            return;
        }
        channel.future.complete(response);
    }
}
