package com.taotao.cloud.sys.api.web.dto.kafka;

/**
 * kafka 监控指标
 */
public interface BrokerTopicMetrics {

    interface BrokerMetrics {
        String bytesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
        String bytesOutPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec";
        String bytesRejectedPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesRejectedPerSec";
        String failedFetchRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec";
        String failedProduceRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec";
        String messagesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec";
        String produceMessageConversionsPerSec = "kafka.server:type=BrokerTopicMetrics,name=ProduceMessageConversionsPerSec";
        String BytesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
        String BytesOutPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec";
        String totalFetchRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=TotalFetchRequestsPerSec";
        String totalProduceRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=TotalProduceRequestsPerSec";
    }

    interface TopicMetrics {
        String bytesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=%s";
        String bytesOutPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec,topic=%s";
        String bytesRejectedPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesRejectedPerSec,topic=%s";
        String failedFetchRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec,topic=%s";
        String failedProduceRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec,topic=%s";
        String messagesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec,topic=%s";
        String produceMessageConversionsPerSec = "kafka.server:type=BrokerTopicMetrics,name=ProduceMessageConversionsPerSec,topic=%s";
        String BytesInPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=%s";
        String BytesOutPerSec = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec,topic=%s";
        String totalFetchRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=TotalFetchRequestsPerSec,topic=%s";
        String totalProduceRequestsPerSec = "kafka.server:type=BrokerTopicMetrics,name=TotalProduceRequestsPerSec,topic=%s";
    }

    interface MBean {
        String COUNT = "Count";
        String EVENT_TYPE = "EventType";
        String FIFTEEN_MINUTE_RATE = "FifteenMinuteRate";
        String FIVE_MINUTE_RATE = "FiveMinuteRate";
        String MEAN_RATE = "MeanRate";
        String ONE_MINUTE_RATE = "OneMinuteRate";
        String RATE_UNIT = "RateUnit";
        String VALUE = "Value";

        /** Messages in /sec. */
        String MESSAGES_IN = "msg";
        /** Bytes in /sec. */
        String BYTES_IN = "ins";
        /** Bytes out /sec. */
        String BYTES_OUT = "out";
        /** Bytes rejected /sec. */
        String BYTES_REJECTED = "rejected";
        /** Failed fetch request /sec. */
        String FAILED_FETCH_REQUEST = "fetch";
        /** Failed produce request /sec. */
        String FAILED_PRODUCE_REQUEST = "produce";

        /** MBean keys. */
        String MESSAGEIN = "message_in";
        String BYTEIN = "byte_in";
        String BYTEOUT = "byte_out";
        String BYTESREJECTED = "byte_rejected";
        String FAILEDFETCHREQUEST = "failed_fetch_request";
        String FAILEDPRODUCEREQUEST = "failed_produce_request";
        String PRODUCEMESSAGECONVERSIONS = "produce_message_conversions";
        String TOTALFETCHREQUESTSPERSEC = "total_fetch_requests";
        String TOTALPRODUCEREQUESTSPERSEC = "total_produce_requests";
        String REPLICATIONBYTESINPERSEC = "replication_bytes_out";
        String REPLICATIONBYTESOUTPERSEC = "replication_bytes_in";
    }

}
