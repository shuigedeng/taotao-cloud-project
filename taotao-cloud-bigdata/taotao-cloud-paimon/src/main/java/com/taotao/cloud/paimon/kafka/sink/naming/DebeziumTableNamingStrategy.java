package com.taotao.cloud.paimon.kafka.sink.naming;

import io.connect.paimon.sink.PaimonSinkConfig;
import io.debezium.data.Envelope;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.paimon.catalog.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Debezium table naming strategy
 */
public class DebeziumTableNamingStrategy implements TableNamingStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebeziumTableNamingStrategy.class);
    private final Pattern sourcePattern = Pattern.compile("\\$\\{(source\\.)(.*?)}");

    @Override
    public Identifier resolveTableName(PaimonSinkConfig config, SinkRecord record) {
        // Default behavior is to replace dots with underscores
        final String topicName = record.topic().replace(".", "_");
        String table = config.getTableNameFormat().replace("${topic}", topicName);
        return resolveTableNameBySource(config, record, table);
    }

    private Identifier resolveTableNameBySource(PaimonSinkConfig config, SinkRecord record, String tableFormat) {
        String table = tableFormat;
        if (table.contains("${source.")) {
            if (isTombstone(record)) {
                LOGGER.warn(
                        "Ignore this record because it seems to be a tombstone that doesn't have source field, then cannot resolve table name in topic '{}', partition '{}', offset '{}'",
                        record.topic(), record.kafkaPartition(), record.kafkaOffset());
                return null;
            }
            try {
                Struct source = ((Struct) record.value()).getStruct(Envelope.FieldName.SOURCE);
                Matcher matcher = sourcePattern.matcher(table);
                while (matcher.find()) {
                    String target = matcher.group();
                    table = table.replace(target, source.getString(matcher.group(2)));
                }
            }
            catch (DataException e) {
                LOGGER.error("Failed to resolve table name with format '{}', check source field in topic '{}'", config.getTableNameFormat(), record.topic(), e);
                throw e;
            }
        }

        final String[] parts = io.debezium.relational.TableId.parseParts(table);
        if (parts.length == 3) {
            return Identifier.create(parts[0] + "_" + parts[1], parts[2]);
        } else if (parts.length == 2){
            return Identifier.create(parts[0], parts[1]);
        } else {
            return Identifier.create("default", parts[2]);
        }
    }

    private boolean isTombstone(SinkRecord record) {
        return record.value() == null;
    }
}
