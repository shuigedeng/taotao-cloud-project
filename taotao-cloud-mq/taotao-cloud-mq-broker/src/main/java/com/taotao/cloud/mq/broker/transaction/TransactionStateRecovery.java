package com.taotao.cloud.mq.broker.transaction;

import com.taotao.cloud.mq.broker.processor.TransactionMessageProcessor;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.plugin.store.log.CommitLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * broker 重启后从存储重建事务状态。
 * <p>
 * 重建路径选的是"回放到盘上那两条内部 topic"，不是"给事务表另开一个文件"：
 * <ul>
 *   <li>半消息与它的定论结论本来就是两条普通的存储记录（{@link TransactionMessageProcessor#HALF_TOPIC}
 *       与 {@link TransactionMessageProcessor#OP_TOPIC}，都落在 0 号队列），
 *       跟着 CommitLog 的恢复扫描一起活下来，不需要第二个持久化真相；</li>
 *   <li>{@code transactionId} 在 {@code MessageCodec} 里没有自己的编码列，但它跟着
 *       properties 那一坨整体落盘、整体读回（{@code MessageCodec#collectProperties} 与解码侧对称），
 *       所以"这条半消息属于哪个事务、它背后的业务 topic/队列是什么"在重启后都还认得；</li>
 *   <li>{@code preparedTransactionOffset} 本支起在 {@code toInner} 里随半消息一起带走，
 *       {@code MessageCodec} 那列也确实会编码落盘、解码读回；但重放路径仍不拿它当抓手——
 *       那要的是"从存储偏移直接定位半消息"，得动存储模块的扫描侧，不在本模块的射程里。</li>
 * </ul>
 * 扫描顺序是先 op 后半消息：已经定论的事务不该再被收进待回查集合（反序也一样能收敛，
 * 因为 {@link TransactionStateManager#recoverHalfMessage} 自己会再看一眼 resolved 表）。
 */
public final class TransactionStateRecovery {

    private static final Logger log = LoggerFactory.getLogger(TransactionStateRecovery.class);

    /** 单次恢复最多扫多少条；半消息与 op 记录各一份。 */
    static final int SCAN_BATCH = 100_000;

    private TransactionStateRecovery() {
    }

    /**
     * 把盘上的半消息与定论记录重放进事务表。
     *
     * @return 重建出来的待回查条数
     */
    public static int recover(CommitLog commitLog, TransactionStateManager manager) {
        if (commitLog == null || manager == null) {
            return 0;
        }
        int resolved = 0;
        List<MessageExt> opRecords =
                commitLog.pullMessage(TransactionMessageProcessor.OP_TOPIC, TransactionMessageProcessor.HALF_QUEUE_ID,
                        0L, SCAN_BATCH);
        for (MessageExt op : opRecords) {
            TransactionStateManager.TxState state = parseState(op);
            String txId = op == null ? null : op.getProperty(TransactionMessageProcessor.PROP_TRANSACTION_ID);
            if (txId == null || state == null) {
                continue;
            }
            manager.recoverResolved(txId, state);
            resolved++;
        }

        int recovered = 0;
        List<MessageExt> halfRecords =
                commitLog.pullMessage(TransactionMessageProcessor.HALF_TOPIC, TransactionMessageProcessor.HALF_QUEUE_ID,
                        0L, SCAN_BATCH);
        for (MessageExt half : halfRecords) {
            if (half == null) {
                continue;
            }
            String txId = half.getProperty(TransactionMessageProcessor.PROP_TRANSACTION_ID);
            String originalTopic = half.getProperty(TransactionMessageProcessor.PROP_ORIGINAL_TOPIC);
            if (txId == null || originalTopic == null) {
                log.warn("half record without transactionId/originalTopic properties skipped: msgId={}",
                        half.getMsgId());
                continue;
            }
            int originalQueueId = parseQueueId(half);
            long born = half.getStoreTimestamp() > 0L ? half.getStoreTimestamp() : half.getBornTimestamp();
            if (manager.recoverHalfMessage(txId, originalTopic, originalQueueId, half, half.getQueueOffset(), born)) {
                recovered++;
            }
        }
        log.info("Transaction state recovered: pending={} alreadySettled={} (scanned half={} op={})",
                recovered, resolved, halfRecords.size(), opRecords.size());
        return recovered;
    }

    private static TransactionStateManager.TxState parseState(MessageExt op) {
        String name = op == null ? null : op.getProperty(TransactionMessageProcessor.PROP_TRANSACTION_STATE);
        if (name == null) {
            return null;
        }
        try {
            return TransactionStateManager.TxState.valueOf(name);
        } catch (IllegalArgumentException e) {
            log.warn("unknown transaction state in op record: {}", name);
            return null;
        }
    }

    private static int parseQueueId(MessageExt half) {
        String raw = half.getProperty(TransactionMessageProcessor.PROP_ORIGINAL_QUEUE_ID);
        if (raw == null) {
            return 0;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            log.warn("unparsable original queueId in half record: {} (msgId={})", raw, half.getMsgId());
            return 0;
        }
    }
}
