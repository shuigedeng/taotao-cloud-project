/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.catalog;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.flink.common.constant.RocketMqCatalogConstant;
import org.apache.rocketmq.remoting.protocol.LanguageCode;
import org.apache.rocketmq.schema.registry.client.SchemaRegistryClient;
import org.apache.rocketmq.schema.registry.client.SchemaRegistryClientFactory;
import org.apache.rocketmq.schema.registry.common.dto.GetSchemaResponse;
import org.apache.rocketmq.schema.registry.common.model.SchemaType;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import org.apache.flink.formats.avro.typeutils.AvroSchemaConverter;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.AbstractCatalog;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.CatalogDatabase;
import org.apache.flink.table.catalog.CatalogDatabaseImpl;
import org.apache.flink.table.catalog.CatalogFunction;
import org.apache.flink.table.catalog.CatalogPartition;
import org.apache.flink.table.catalog.CatalogPartitionImpl;
import org.apache.flink.table.catalog.CatalogPartitionSpec;
import org.apache.flink.table.catalog.CatalogTable;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.catalog.exceptions.DatabaseAlreadyExistException;
import org.apache.flink.table.catalog.exceptions.DatabaseNotEmptyException;
import org.apache.flink.table.catalog.exceptions.DatabaseNotExistException;
import org.apache.flink.table.catalog.exceptions.FunctionAlreadyExistException;
import org.apache.flink.table.catalog.exceptions.FunctionNotExistException;
import org.apache.flink.table.catalog.exceptions.PartitionAlreadyExistsException;
import org.apache.flink.table.catalog.exceptions.PartitionNotExistException;
import org.apache.flink.table.catalog.exceptions.PartitionSpecInvalidException;
import org.apache.flink.table.catalog.exceptions.TableAlreadyExistException;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;
import org.apache.flink.table.catalog.exceptions.TableNotPartitionedException;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.table.expressions.Expression;
import org.apache.flink.table.factories.Factory;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.FieldsDataType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.utils.TypeConversions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;
import static org.apache.flink.util.StringUtils.isNullOrWhitespaceOnly;

/** A catalog implementation for RocketMQ. */
public class RocketMQCatalog extends AbstractCatalog {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQCatalog.class);
    public static final String DEFAULT_DB = "default";
    public final String namesrvAddr;
    private final String schemaRegistryUrl;
    private DefaultMQAdminExt mqAdminExt;
    private SchemaRegistryClient schemaRegistryClient;

    public RocketMQCatalog(
            String catalogName, String database, String namesrvAddr, String schemaRegistryUrl) {
        super(catalogName, database);

        checkArgument(!isNullOrWhitespaceOnly(namesrvAddr), "namesrvAddr cannot be null or empty");
        checkArgument(
                !isNullOrWhitespaceOnly(schemaRegistryUrl),
                "schemaRegistryUrl cannot be null or empty");

        this.namesrvAddr = namesrvAddr;
        this.schemaRegistryUrl = schemaRegistryUrl;
        LOG.info("Created RocketMQ Catalog {}", catalogName);
    }

    @Override
    public Optional<Factory> getFactory() {
        return Optional.of(new RocketMQCatalogFactory());
    }

    @Override
    public void open() throws CatalogException {
        if (mqAdminExt == null) {
            try {
                mqAdminExt = new DefaultMQAdminExt();
                mqAdminExt.setNamesrvAddr(namesrvAddr);
                mqAdminExt.setLanguage(LanguageCode.JAVA);
                mqAdminExt.start();
            } catch (MQClientException e) {
                throw new CatalogException(
                        "Failed to create RocketMQ admin using :" + namesrvAddr, e);
            }
        }
        if (schemaRegistryClient == null) {
            schemaRegistryClient = SchemaRegistryClientFactory.newClient(schemaRegistryUrl, null);
        }
    }

    @Override
    public void close() throws CatalogException {
        if (Objects.nonNull(mqAdminExt)) {
            mqAdminExt.shutdown();
            mqAdminExt = null;
        }
        if (Objects.nonNull(schemaRegistryClient)) {
            schemaRegistryClient = null;
        }
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return Collections.singletonList(getDefaultDatabase());
    }

    @Override
    public CatalogDatabase getDatabase(String databaseName)
            throws DatabaseNotExistException, CatalogException {
        if (StringUtils.isEmpty(databaseName)) {
            throw new CatalogException("Database name can not be null or empty.");
        }
        if (!databaseExists(databaseName)) {
            throw new DatabaseNotExistException(getName(), databaseName);
        } else {
            return new CatalogDatabaseImpl(new HashMap<>(), null);
        }
    }

    @Override
    public boolean databaseExists(String databaseName) throws CatalogException {
        return getDefaultDatabase().equals(databaseName);
    }

    @Override
    public List<String> listTables(String databaseName)
            throws DatabaseNotExistException, CatalogException {
        if (!getDefaultDatabase().equals(databaseName)) {
            throw new DatabaseNotExistException(getName(), databaseName);
        }
        try {
            List<String> tables = schemaRegistryClient.getSubjectsByTenant("default", "default");
            return tables;
        } catch (Exception e) {
            throw new CatalogException(
                    String.format(
                            "Failed to get topics of namespace %s from schema registry client.",
                            databaseName),
                    e);
        }
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath tablePath)
            throws TableNotExistException, CatalogException {
        if (!tableExists(tablePath)) {
            throw new TableNotExistException(getName(), tablePath);
        }
        String subject = tablePath.getObjectName();
        try {
            GetSchemaResponse getSchemaResponse = schemaRegistryClient.getSchemaBySubject(subject);
            if (getSchemaResponse.getType() != SchemaType.AVRO) {
                throw new CatalogException("Only support avro schema.");
            }
            return getCatalogTableForSchema(subject, getSchemaResponse);
        } catch (Exception e) {
            throw new CatalogException(
                    String.format(
                            "Failed to get schema of table %s from schema registry client.",
                            tablePath.getFullName()),
                    e);
        }
    }

    private CatalogTable getCatalogTableForSchema(
            String topic, GetSchemaResponse getSchemaResponse) {
        DataType dataType = AvroSchemaConverter.convertToDataType(getSchemaResponse.getIdl());
        Schema.Builder builder = Schema.newBuilder();
        if (dataType instanceof FieldsDataType) {
            FieldsDataType fieldsDataType = (FieldsDataType) dataType;
            RowType rowType = (RowType) fieldsDataType.getLogicalType();
            for (RowType.RowField field : rowType.getFields()) {
                DataType toDataType = TypeConversions.fromLogicalToDataType(field.getType());
                builder.column(field.getName(), toDataType);
            }
        }
        Schema schema = builder.build();
        Map<String, String> options = new HashMap<>();
        options.put(RocketMqCatalogConstant.CONNECTOR, RocketMqCatalogConstant.ROCKETMQ_CONNECTOR);
        options.put(RocketMqCatalogConstant.TOPIC, topic);
        options.put(RocketMqCatalogConstant.NAME_SERVER_ADDRESS, mqAdminExt.getNamesrvAddr());
        return CatalogTable.of(schema, null, Collections.emptyList(), options);
    }

    @Override
    public boolean tableExists(ObjectPath tablePath) throws CatalogException {
        if (!getDefaultDatabase().equals(tablePath.getDatabaseName())) {
            throw new CatalogException("Database name is not default.");
        }
        if (StringUtils.isEmpty(tablePath.getObjectName())) {
            return false;
        }
        String subject = tablePath.getObjectName();
        try {
            GetSchemaResponse getSchemaResponse = schemaRegistryClient.getSchemaBySubject(subject);
            if (Objects.nonNull(getSchemaResponse)) {
                return true;
            }
        } catch (Exception e) {
            throw new CatalogException(
                    String.format(
                            "Failed to get schema of table %s from schema registry client.",
                            tablePath.getFullName()),
                    e);
        }
        return false;
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath tablePath)
            throws TableNotExistException, TableNotPartitionedException, CatalogException {
        checkNotNull(tablePath, "Table path cannot be null");

        try {
            TopicStatsTable topicStatsTable =
                    mqAdminExt.examineTopicStats(tablePath.getObjectName());
            return topicStatsTable.getOffsetTable().keySet().stream()
                    .map(
                            topicOffset ->
                                    new CatalogPartitionSpec(
                                            new HashMap<String, String>(1) {
                                                {
                                                    String queueId =
                                                            String.valueOf(
                                                                    topicOffset.getQueueId());
                                                    put("__queue_id__", queueId);
                                                }
                                            }))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CatalogException(
                    String.format(
                            "Failed to list partitions of table %s by defaultMQAdminExt.",
                            tablePath.getFullName()),
                    e);
        }
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(
            ObjectPath tablePath, CatalogPartitionSpec partitionSpec)
            throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return listPartitions(tablePath);
    }

    @Override
    public CatalogPartition getPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec)
            throws PartitionNotExistException, CatalogException {
        return new CatalogPartitionImpl(partitionSpec.getPartitionSpec(), null);
    }

    @Override
    public boolean partitionExists(ObjectPath tablePath, CatalogPartitionSpec partitionSpec)
            throws CatalogException {
        try {
            List<CatalogPartitionSpec> catalogPartitionSpecs = listPartitions(tablePath);
            return catalogPartitionSpecs.contains(partitionSpec);
        } catch (Exception e) {
            throw new CatalogException(
                    String.format(
                            "Failed to judge partition %s of table %s exists by defaultMQAdminExt.",
                            partitionSpec, tablePath.getFullName()),
                    e);
        }
    }

    // ------------------------------------------------------------------------
    // Unsupported catalog operations for RocketMQ
    // There should not be such permission in the connector, it is very dangerous
    // ------------------------------------------------------------------------

    @Override
    public void createDatabase(String name, CatalogDatabase database, boolean ignoreIfExists)
            throws DatabaseAlreadyExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dropDatabase(String name, boolean ignoreIfNotExists, boolean cascade)
            throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createTable(ObjectPath tablePath, CatalogBaseTable table, boolean ignoreIfExists)
            throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dropTable(ObjectPath tablePath, boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> listFunctions(String dbName)
            throws DatabaseNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CatalogFunction getFunction(ObjectPath functionPath)
            throws FunctionNotExistException, CatalogException {
        throw new FunctionNotExistException("Not support to find functions.", functionPath);
    }

    @Override
    public boolean functionExists(ObjectPath functionPath) throws CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFunction(
            ObjectPath functionPath, CatalogFunction function, boolean ignoreIfExists)
            throws FunctionAlreadyExistException, DatabaseNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterFunction(
            ObjectPath functionPath, CatalogFunction newFunction, boolean ignoreIfNotExists)
            throws FunctionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dropFunction(ObjectPath functionPath, boolean ignoreIfNotExists)
            throws FunctionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterDatabase(String name, CatalogDatabase newDatabase, boolean ignoreIfNotExists)
            throws DatabaseNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> listViews(String databaseName)
            throws DatabaseNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterTable(
            ObjectPath tablePath, CatalogBaseTable newTable, boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renameTable(ObjectPath tablePath, String newTableName, boolean ignoreIfNotExists)
            throws TableNotExistException, TableAlreadyExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CatalogPartitionSpec> listPartitionsByFilter(
            ObjectPath tablePath, List<Expression> expressions)
            throws TableNotExistException, TableNotPartitionedException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createPartition(
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            CatalogPartition partition,
            boolean ignoreIfExists)
            throws TableNotExistException, TableNotPartitionedException,
                    PartitionSpecInvalidException, PartitionAlreadyExistsException,
                    CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dropPartition(
            ObjectPath tablePath, CatalogPartitionSpec partitionSpec, boolean ignoreIfNotExists)
            throws PartitionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterPartition(
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            CatalogPartition newPartition,
            boolean ignoreIfNotExists)
            throws PartitionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CatalogTableStatistics getTableStatistics(ObjectPath tablePath)
            throws TableNotExistException, CatalogException {
        return CatalogTableStatistics.UNKNOWN;
    }

    @Override
    public CatalogColumnStatistics getTableColumnStatistics(ObjectPath tablePath)
            throws TableNotExistException, CatalogException {
        return CatalogColumnStatistics.UNKNOWN;
    }

    @Override
    public CatalogTableStatistics getPartitionStatistics(
            ObjectPath tablePath, CatalogPartitionSpec partitionSpec)
            throws PartitionNotExistException, CatalogException {
        return CatalogTableStatistics.UNKNOWN;
    }

    @Override
    public CatalogColumnStatistics getPartitionColumnStatistics(
            ObjectPath tablePath, CatalogPartitionSpec partitionSpec)
            throws PartitionNotExistException, CatalogException {
        return CatalogColumnStatistics.UNKNOWN;
    }

    @Override
    public void alterTableStatistics(
            ObjectPath tablePath, CatalogTableStatistics tableStatistics, boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterTableColumnStatistics(
            ObjectPath tablePath,
            CatalogColumnStatistics columnStatistics,
            boolean ignoreIfNotExists)
            throws TableNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterPartitionStatistics(
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            CatalogTableStatistics partitionStatistics,
            boolean ignoreIfNotExists)
            throws PartitionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void alterPartitionColumnStatistics(
            ObjectPath tablePath,
            CatalogPartitionSpec partitionSpec,
            CatalogColumnStatistics columnStatistics,
            boolean ignoreIfNotExists)
            throws PartitionNotExistException, CatalogException {
        throw new UnsupportedOperationException();
    }
}
