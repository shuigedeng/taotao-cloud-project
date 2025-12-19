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

package com.taotao.cloud.elasticsearch.plugin.aa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.elasticsearch.client.internal.Client;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.watcher.ResourceWatcherService;
import org.elasticsearch.xcontent.NamedXContentRegistry;

/**
 * FileIndexStorePlugin
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class FileIndexStorePlugin extends Plugin {

    private final Settings settings;

    public FileIndexStorePlugin( Settings settings ) {
        this.settings = settings;
    }

    @Override
    public Collection<Object> createComponents(
            Client client,
            ClusterService clusterService,
            ThreadPool threadPool,
            ResourceWatcherService resourceWatcherService,
            ScriptService scriptService,
            NamedXContentRegistry xContentRegistry ) {
        List<Object> components = new ArrayList<>();
        components.add(new FileIndexStore());
        return components;
    }

    @Override
    public List<NamedWriteableRegistry.Entry> getNamedWriteables() {
        List<NamedWriteableRegistry.Entry> namedWriteables = new ArrayList<>();
        namedWriteables.add(
                new NamedWriteableRegistry.Entry(IndexStore.class, "file", FileIndexStore::new));
        return namedWriteables;
    }

    private class FileIndexStore implements IndexStore {

        private final String dataDir;

        FileIndexStore() {
            dataDir = settings.get("index.store.file.data_dir", "./data");
        }

        @Override
        public void delete( String index, String id ) throws IOException {
            File file = new File(dataDir, index + "/" + id);
            Files.deleteIfExists(file.toPath());
        }

        @Override
        public void delete( String index ) throws IOException {
            File file = new File(dataDir, index);
            FileUtils.deleteDirectory(file);
        }

        @Override
        public IndexOutput createOutput( String index, String id ) throws IOException {
            File file = new File(dataDir, index + "/" + id);
            file.getParentFile().mkdirs();
            return new FileOutputStreamIndexOutput(file);
        }

        @Override
        public IndexInput openInput( String index, String id ) throws IOException {
            File file = new File(dataDir, index + "/" + id);
            if (!file.exists()) {
                throw new FileNotFoundException(file.getAbsolutePath());
            }
            return new FileInputStreamIndexInput(file);
        }

        @Override
        public void close() throws IOException {
            // do nothing
        }
    }
}
