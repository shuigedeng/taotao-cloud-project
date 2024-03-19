package com.taotao.cloud.flink.cep.event;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class EventDeSerializationSchema implements KafkaRecordDeserializationSchema<Event> {

    private static final long serialVersionUID = 6154188370181669758L;

    @Override
    public TypeInformation<Event> getProducedType() {
        return TypeInformation.of(Event.class);
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        KafkaRecordDeserializationSchema.super.open(context);
    }

    @Override
    public void deserialize(
            ConsumerRecord<byte[], byte[]> consumerRecord, Collector<Event> collector)
            throws IOException {
        collector.collect(Event.fromString(new String(consumerRecord.value())));
    }
}
