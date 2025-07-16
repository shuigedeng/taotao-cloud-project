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

import io.debezium.data.Bits;
import io.debezium.time.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.paimon.types.DataType;
import org.apache.paimon.types.DataTypes;
import org.apache.paimon.types.DecimalType;
import org.apache.paimon.utils.DateTimeUtils;

/**
 * Utils to handle 'schema' field in debezium Json. TODO: The methods have many duplicate codes with
 * MySqlRecordParser. Need refactor.
 */
public class DebeziumSchemaUtils {

    /** Transform raw string value according to schema. */
    public static String transformRawValue(
            @Nullable String rawValue,
            Schema.Type type,
            @Nullable String className,
            ZoneId serverTimeZone) {
        if (rawValue == null) {
            return null;
        }

        String transformed = rawValue;

        if (Bits.LOGICAL_NAME.equals(className)) {
            // transform little-endian form to normal order
            // https://debezium.io/documentation/reference/stable/connectors/mysql.html#mysql-data-types
            byte[] littleEndian = Base64.getDecoder().decode(rawValue);
            byte[] bigEndian = new byte[littleEndian.length];
            for (int i = 0; i < littleEndian.length; i++) {
                bigEndian[i] = littleEndian[littleEndian.length - 1 - i];
            }
            transformed = Base64.getEncoder().encodeToString(bigEndian);
        } else if (type == Schema.Type.BYTES && className == null) {
            // MySQL binary, varbinary, blob
            transformed = new String(Base64.getDecoder().decode(rawValue));
        } else if (type == Schema.Type.BYTES && decimalLogicalName().equals(className)) {
            // MySQL numeric, fixed, decimal
            try {
                new BigDecimal(rawValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid big decimal value "
                                + rawValue
                                + ". Make sure that in the `customConverterConfigs` "
                                + "of the JsonDebeziumDeserializationSchema you created, set '"
                                + JsonConverterConfig.DECIMAL_FORMAT_CONFIG
                                + "' to 'numeric'",
                        e);
            }
        }
        // pay attention to the temporal types
        // https://debezium.io/documentation/reference/stable/connectors/mysql.html#mysql-temporal-types
        else if (Date.SCHEMA_NAME.equals(className)) {
            // MySQL date
            transformed = DateTimeUtils.toLocalDate(Integer.parseInt(rawValue)).toString();
        } else if (Timestamp.SCHEMA_NAME.equals(className)) {
            // MySQL datetime (precision 0-3)

            // display value of datetime is not affected by timezone, see
            // https://dev.mysql.com/doc/refman/8.0/en/datetime.html for standard, and
            // RowDataDebeziumDeserializeSchema#convertToTimestamp in flink-cdc-connector
            // for implementation
            LocalDateTime localDateTime =
                    DateTimeUtils.toLocalDateTime(Long.parseLong(rawValue), ZoneOffset.UTC);
            transformed = DateTimeUtils.formatLocalDateTime(localDateTime, 3);
        } else if (MicroTimestamp.SCHEMA_NAME.equals(className)) {
            // MySQL datetime (precision 4-6)
            long microseconds = Long.parseLong(rawValue);
            long microsecondsPerSecond = 1_000_000;
            long nanosecondsPerMicros = 1_000;
            long seconds = microseconds / microsecondsPerSecond;
            long nanoAdjustment = (microseconds % microsecondsPerSecond) * nanosecondsPerMicros;

            // display value of datetime is not affected by timezone, see
            // https://dev.mysql.com/doc/refman/8.0/en/datetime.html for standard, and
            // RowDataDebeziumDeserializeSchema#convertToTimestamp in flink-cdc-connector
            // for implementation
            LocalDateTime localDateTime =
                    Instant.ofEpochSecond(seconds, nanoAdjustment)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDateTime();
            transformed = DateTimeUtils.formatLocalDateTime(localDateTime, 6);
        } else if (ZonedTimestamp.SCHEMA_NAME.equals(className)) {
            // MySQL timestamp

            // display value of timestamp is affected by timezone, see
            // https://dev.mysql.com/doc/refman/8.0/en/datetime.html for standard, and
            // RowDataDebeziumDeserializeSchema#convertToTimestamp in flink-cdc-connector
            // for implementation
            LocalDateTime localDateTime =
                    Instant.parse(rawValue).atZone(serverTimeZone).toLocalDateTime();
            transformed = DateTimeUtils.formatLocalDateTime(localDateTime, 6);
        } else if (MicroTime.SCHEMA_NAME.equals(className)) {
            long microseconds = Long.parseLong(rawValue);
            long microsecondsPerSecond = 1_000_000;
            long nanosecondsPerMicros = 1_000;
            long seconds = microseconds / microsecondsPerSecond;
            long nanoAdjustment = (microseconds % microsecondsPerSecond) * nanosecondsPerMicros;

            transformed =
                    Instant.ofEpochSecond(seconds, nanoAdjustment)
                            .atZone(ZoneOffset.UTC)
                            .toLocalTime()
                            .toString();
        }

        return transformed;
    }

    public static DataType toDataType(Field field) {
        Schema schema = field.schema();
        // Field type
        Schema.Type type = schema.type();
        // Extract logical type name
        String logicalTypeName = schema.name();
        // Field parameters
        Map<String, String> parameters = schema.parameters();

        if (logicalTypeName == null) {
            return fromDebeziumType(type);
        }

        if (Bits.LOGICAL_NAME.equals(logicalTypeName)) {
            int length = Integer.parseInt(parameters.get("length"));
            return DataTypes.BINARY((length + 7) / 8);
        }

        if (decimalLogicalName().equals(logicalTypeName)) {
            String precision = parameters.get("connect.decimal.precision");
            if (precision == null) {
                return DataTypes.DECIMAL(20, 0);
            }

            int p = Integer.parseInt(precision);
            if (p > DecimalType.MAX_PRECISION) {
                return DataTypes.STRING();
            } else {
                int scale = Integer.parseInt(parameters.get("scale"));
                return DataTypes.DECIMAL(p, scale);
            }
        }

        if (Date.SCHEMA_NAME.equals(logicalTypeName)) {
            return DataTypes.DATE();
        }

        if (Timestamp.SCHEMA_NAME.equals(logicalTypeName)) {
            return DataTypes.TIMESTAMP(3);
        }

        if (MicroTimestamp.SCHEMA_NAME.equals(logicalTypeName)
                || ZonedTimestamp.SCHEMA_NAME.equals(logicalTypeName)) {
            return DataTypes.TIMESTAMP(6);
        }

        if (MicroTime.SCHEMA_NAME.equals(logicalTypeName)) {
            return DataTypes.TIME();
        }

        return fromDebeziumType(type);
    }

    private static DataType fromDebeziumType(Schema.Type type) {
        switch (type) {
            case INT8:
                return DataTypes.TINYINT();
            case INT16:
                return DataTypes.SMALLINT();
            case INT32:
                return DataTypes.INT();
            case INT64:
                return DataTypes.BIGINT();
            case FLOAT32:
                return DataTypes.FLOAT();
            case FLOAT64:
                return DataTypes.DOUBLE();
            case BOOLEAN:
                return DataTypes.BOOLEAN();
            case BYTES:
                return DataTypes.BYTES();
            case STRING:
            default:
                return DataTypes.STRING();
        }
    }

    /**
     * get decimal logical name.
     *
     * <p>Using the maven shade plugin will shade the constant value. see <a
     * href="https://issues.apache.org/jira/browse/MSHADE-156">...</a> so the string
     * org.apache.kafka.connect.data.Decimal is shaded to org.apache.flink.kafka.shaded
     * .org.apache.kafka.connect.data.Decimal.
     */
    public static String decimalLogicalName() {
        return "org.apache.#.connect.data.Decimal".replace("#", "kafka");
    }
}
