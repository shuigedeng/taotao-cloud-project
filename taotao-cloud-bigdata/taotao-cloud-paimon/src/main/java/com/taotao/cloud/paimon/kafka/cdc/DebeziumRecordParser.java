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

package com.taotao.cloud.paimon.kafka.cdc;

import static org.apache.paimon.utils.JsonSerdeUtil.writeValueAsString;

import com.taotao.cloud.paimon.kafka.data.CdcRecord;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.paimon.data.GenericRow;
import org.apache.paimon.schema.Schema;
import org.apache.paimon.shade.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.paimon.types.DataField;
import org.apache.paimon.types.DataType;
import org.apache.paimon.types.RowKind;
import org.apache.paimon.utils.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebeziumRecordParser {

    private static final String FIELD_BEFORE = "before";
    private static final String FIELD_AFTER = "after";
    private static final String FIELD_TYPE = "op";
    private static final String OP_INSERT = "c";
    private static final String OP_UPDATE = "u";
    private static final String OP_DELETE = "d";
    private static final String OP_READ = "r";

    private static final Logger LOG = LoggerFactory.getLogger(DebeziumRecordParser.class);

    /**
     * Build schema for create table
     * @param record
     * @return
     */
    public static Schema buildSchema(SinkRecord record) {
        org.apache.kafka.connect.data.Schema schema = record.valueSchema();
        if (schema.type() != org.apache.kafka.connect.data.Schema.Type.STRUCT) {
            throw new ConnectException("Record value schema must be struct!");
        }
        Schema.Builder builder = Schema.newBuilder();
        List<Field> fields = record.valueSchema().fields();
        for (Field field : fields) {
            builder.column(
                    field.name(), DebeziumSchemaUtils.toDataType(field), field.schema().doc());
        }
        // build schema
        return builder.build();
    }

    public static Optional<GenericRow> toGenericRow(
            CdcRecord cdcRecord, List<DataField> dataFields) {
        Struct struct = cdcRecord.value();
        GenericRow genericRow = new GenericRow(cdcRecord.kind(), dataFields.size());
        List<String> fieldNames =
                dataFields.stream().map(DataField::name).collect(Collectors.toList());
        List<Field> fields = struct.schema().fields();
        for (Field field : fields) {
            String key = field.name();
            String value = getValueAsString(struct.get(key));

            int idx = fieldNames.indexOf(key);
            if (idx < 0) {
                LOG.info("Field " + key + " not found. Waiting for schema update.");
                return Optional.empty();
            }

            if (value == null) {
                continue;
            }

            DataType type = dataFields.get(idx).type();
            // TODO TypeUtils.castFromString cannot deal with complex types like arrays and
            //  maps. Change type of CdcRecord#field if needed.
            try {
                genericRow.setField(idx, TypeUtils.castFromCdcValueString(value, type));
            } catch (Exception e) {
                LOG.info(
                        "Failed to convert value "
                                + value
                                + " to type "
                                + type
                                + ". Waiting for schema update.",
                        e);
                return Optional.empty();
            }
        }
        return Optional.of(genericRow);
    }

    private static String getValueAsString(Object value) {
        if (Objects.nonNull(value) && !TypeUtils.isBasicType(value)) {
            try {
                return writeValueAsString(value);
            } catch (JsonProcessingException e) {
                LOG.error("Failed to deserialize record.", e);
                return Objects.toString(value);
            }
        }
        return Objects.toString(value);
    }

    public static List<CdcRecord> extractRecords(SinkRecord record) {
        List<CdcRecord> cdcRecords = new ArrayList<>();
        Struct value = (Struct) record.value();
        String op = (String) value.get(FIELD_TYPE);
        switch (op) {
            case OP_DELETE:
                processRecords(getBefore(value), RowKind.DELETE, cdcRecords);
                break;
            case OP_READ:
            case OP_INSERT:
                processRecords(getData(value), RowKind.INSERT, cdcRecords);
                break;
            case OP_UPDATE:
                // before
                processRecords(getBefore(value), RowKind.DELETE, cdcRecords);
                // after
                processRecords(getData(value), RowKind.INSERT, cdcRecords);
                break;
        }
        return cdcRecords;
    }

    private static Struct getData(Struct value) {
        return (Struct) value.get(FIELD_AFTER);
    }

    private static Struct getBefore(Struct value) {
        return (Struct) value.get(FIELD_BEFORE);
    }

    private static void processRecords(Struct value, RowKind rowKind, List<CdcRecord> cdcRecords) {
        cdcRecords.add(new CdcRecord(rowKind, value));
    }
}
