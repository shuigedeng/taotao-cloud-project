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

package com.taotao.cloud.paimon.kafka.sink;

import com.taotao.cloud.paimon.kafka.cdc.DebeziumRecordParser;
import com.taotao.cloud.paimon.kafka.data.CdcRecord;
import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.paimon.catalog.Catalog;
import org.apache.paimon.catalog.Identifier;
import org.apache.paimon.data.GenericRow;
import org.apache.paimon.schema.Schema;
import org.apache.paimon.table.FileStoreTable;
import org.apache.paimon.table.Table;
import org.apache.paimon.table.sink.CommitMessage;
import org.apache.paimon.table.sink.StreamTableCommit;
import org.apache.paimon.table.sink.StreamTableWrite;
import org.apache.paimon.table.sink.StreamWriteBuilder;

/**
 * Paimon table writer
 */
public class PaimonTableWriter implements AutoCloseable {
    private final Catalog catalog;
    private final PaimonSinkConfig config;
    private Map<Identifier, Table> cacheTables;
    private long commitIdentifier = 0;

    public PaimonTableWriter(Catalog catalog, PaimonSinkConfig config) {
        this.catalog = catalog;
        this.config = config;
        this.cacheTables = new HashMap<>();
    }

    public void write(SinkRecord record) throws Exception {
        Identifier identifier = tableIdentifier(record);
        // Build paimon table schema
        Schema schema = DebeziumRecordParser.buildSchema(record);
        // extract cdc record
        List<CdcRecord> records = DebeziumRecordParser.extractRecords(record);
        // create table if not cache and return
        FileStoreTable table = (FileStoreTable) loadTableIfNotCacheAndReturn(identifier, schema);
        StreamWriteBuilder writeBuilder = table.newStreamWriteBuilder();
        // new write
        StreamTableWrite write = writeBuilder.newWrite();
        // write record
        for (CdcRecord cdcRecord : records) {
            Optional<GenericRow> optionalGenericRow =
                    DebeziumRecordParser.toGenericRow(cdcRecord, schema.fields());
            if (optionalGenericRow.isPresent()) {
                write.write(optionalGenericRow.get());
            }
        }
        List<CommitMessage> messages = write.prepareCommit(false, commitIdentifier);
        commitIdentifier++;
        // 3. Collect all CommitMessages to a global node and commit
        StreamTableCommit commit = writeBuilder.newCommit();
        commit.commit(commitIdentifier, messages);
    }

    private Table loadTableIfNotCacheAndReturn(Identifier identifier, Schema schema) {
        if (!cacheTables.containsKey(identifier)) {
            Table table = null;
            try {
                table = catalog.getTable(identifier);
            } catch (Catalog.TableNotExistException e) {
                // Enabled auto create table
                if (config.isAutoCreate()) {
                    try {
                        catalog.createTable(identifier, schema, false);
                        table = catalog.getTable(identifier);
                    } catch (Catalog.TableAlreadyExistException
                            | Catalog.DatabaseNotExistException
                            | Catalog.TableNotExistException ex) {
                        throw new ConnectException(ex);
                    }
                }
            }
            cacheTables.put(identifier, table);
        }
        return cacheTables.get(identifier);
    }

    private Identifier tableIdentifier(SinkRecord record) {
        return config.getTableNamingStrategy().resolveTableName(config, record);
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * <p>While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     *
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it would cause problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     *
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        if (!cacheTables.isEmpty()) {
            cacheTables.clear();
        }
    }
}
