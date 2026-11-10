package com.taotao.cloud.mq.client.consumer.rebalance;

import com.taotao.cloud.mq.common.MessageQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * 平均分配策略（对标 RocketMQ AllocateMessageQueueAveragely）.
 * <p>
 * 将队列按 Consumer 数量均匀切割:
 * <pre>
 *   mqAll: [q0, q1, q2, q3, q4, q5, q6, q7]
 *   cidAll: [c0, c1, c2]
 *
 *   c0 → [q0, q1, q2]
 *   c1 → [q3, q4, q5]
 *   c2 → [q6, q7]
 * </pre>
 * 余数从前面的 Consumer 开始分配。
 */
public class AllocateMessageQueueAveragely implements AllocateMessageQueueStrategy {

    @Override
    public List<MessageQueue> allocate(
            String consumerGroup,
            String currentCID,
            List<MessageQueue> mqAll,
            List<String> cidAll) {

        if (mqAll == null || mqAll.isEmpty()) {
            return new ArrayList<>();
        }
        if (cidAll == null || cidAll.isEmpty()) {
            return new ArrayList<>();
        }
        if (!cidAll.contains(currentCID)) {
            return new ArrayList<>();
        }

        // 确保 cidAll 已排序 (约定调用方传入已排序列表，此处双重保险)
        List<String> sortedCID = new ArrayList<>(cidAll);
        sortedCID.sort(String::compareTo);

        int index = sortedCID.indexOf(currentCID);
        if (index < 0) {
            return new ArrayList<>();
        }

        int mqSize = mqAll.size();
        int cidSize = sortedCID.size();

        // 计算每个 consumer 应分配的队列数量
        int avgSize = mqSize / cidSize;
        // 余数: 前面 index 个 consumer 各多分 1 个
        int remainder = mqSize % cidSize;

        int startIndex;
        int allocateSize;
        if (index < remainder) {
            // 余数部分: index 之前已分配了 avgSize+1, 从第 (avgSize+1)*index 开始
            allocateSize = avgSize + 1;
            startIndex = index * allocateSize;
        } else {
            // 非余数部分
            allocateSize = avgSize;
            startIndex = remainder * (avgSize + 1) + (index - remainder) * avgSize;
        }

        List<MessageQueue> result = new ArrayList<>();
        for (int i = 0; i < allocateSize && startIndex + i < mqSize; i++) {
            result.add(mqAll.get(startIndex + i));
        }
        return result;
    }

    @Override
    public String getName() {
        return "AVG";
    }
}
