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

import com.taotao.cloud.paimon.kafka.common.PropertyUtil;
import com.taotao.cloud.paimon.kafka.sink.naming.DebeziumTableNamingStrategy;
import com.taotao.cloud.paimon.kafka.sink.naming.TableNamingStrategy;
import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

/**
 * Paimon sink config
 */
public class PaimonSinkConfig extends AbstractConfig {

    // config options
    private static final String CATALOG_PROP_PREFIX = "paimon.catalog.";
    private static final String HADOOP_PROP_PREFIX = "paimon.hadoop.";

    // Auto  create table
    public static final String AUTO_CREATE = "auto.create";
    private static final String AUTO_CREATE_DEFAULT = "false";
    private static final String AUTO_CREATE_DOC =
            "Whether to automatically create the destination table based on record schema if it is "
                    + "found to be missing by issuing ``CREATE``.";

    // schema evolution
    public static final String AUTO_EVOLVE = "auto.evolve";
    private static final String AUTO_EVOLVE_DEFAULT = "false";
    private static final String AUTO_EVOLVE_DOC =
            "Whether to automatically add columns in the table schema when found to be missing relative "
                    + "to the record schema by issuing ``ALTER``.";

    // Table name format
    public static final String TABLE_NAME_FORMAT_FIELD = "table.name.format";
    private static final String TABLE_NAME_FORMAT_FIELD_DEFAULT = "${topic}";
    private static final String TABLE_NAME_FORMAT_FIELD_DOC =
            "A format string for the table, which may contain '${topic}' as a placeholder for the original topic name.";

    // Table naming strategy
    public static final String TABLE_NAMING_STRATEGY_FIELD = "table.naming.strategy";
    public static final String TABLE_NAMING_STRATEGY_FIELD_DEFAULT =
            DebeziumTableNamingStrategy.class.getName();
    public static final String TABLE_NAMING_STRATEGY_FIELD_DOC =
            "Name of the strategy class that implements the TablingNamingStrategy interface";

    // parameter
    public static final ConfigDef CONFIG_DEF = newConfigDef();
    private final Map<String, String> originalProps;
    private final Map<String, String> catalogProps;
    private final Map<String, String> hadoopProps;
    // table naming strategy
    private final TableNamingStrategy tableNamingStrategy;
    // auto create table
    private final boolean autoCreate;
    // schema evolution
    private final boolean autoEvolve;
    // table name format
    private final String tableNameFormat;

    public static String version() {
        String version = PaimonSinkConfig.class.getPackage().getImplementationVersion();
        if (version == null) {
            version = "unknown";
        }
        return version;
    }

    public PaimonSinkConfig(Map<String, String> originalProps) {
        super(CONFIG_DEF, originalProps);
        this.originalProps = originalProps;
        // Table naming strategy
        this.tableNamingStrategy =
                getConfiguredInstance(TABLE_NAMING_STRATEGY_FIELD, TableNamingStrategy.class);
        // Catalog props
        this.catalogProps = PropertyUtil.propertiesWithPrefix(originalProps, CATALOG_PROP_PREFIX);
        this.hadoopProps = PropertyUtil.propertiesWithPrefix(originalProps, HADOOP_PROP_PREFIX);
        // auto create table
        this.autoCreate = getBoolean(AUTO_CREATE);
        // schema evolution
        this.autoEvolve = getBoolean(AUTO_EVOLVE);
        this.tableNameFormat = this.getString(TABLE_NAME_FORMAT_FIELD);
    }

    private static ConfigDef newConfigDef() {
        ConfigDef configDef = new ConfigDef();

        // table name strategy
        configDef
                .define(
                        TABLE_NAMING_STRATEGY_FIELD,
                        ConfigDef.Type.CLASS,
                        TABLE_NAMING_STRATEGY_FIELD_DEFAULT,
                        ConfigDef.Importance.MEDIUM,
                        TABLE_NAMING_STRATEGY_FIELD_DOC)
                .define(
                        AUTO_EVOLVE,
                        ConfigDef.Type.BOOLEAN,
                        AUTO_EVOLVE_DEFAULT,
                        ConfigDef.Importance.MEDIUM,
                        AUTO_EVOLVE_DOC)
                .define(
                        AUTO_CREATE,
                        ConfigDef.Type.BOOLEAN,
                        AUTO_CREATE_DEFAULT,
                        ConfigDef.Importance.MEDIUM,
                        AUTO_CREATE_DOC)
                .define(
                        TABLE_NAME_FORMAT_FIELD,
                        ConfigDef.Type.STRING,
                        TABLE_NAME_FORMAT_FIELD_DEFAULT,
                        ConfigDef.Importance.MEDIUM,
                        TABLE_NAME_FORMAT_FIELD_DOC);
        // Config options
        return configDef;
    }

    public Map<String, String> originalProps() {
        return originalProps;
    }

    public Map<String, String> catalogProps() {
        return catalogProps;
    }

    public Map<String, String> hadoopProps() {
        return hadoopProps;
    }

    public TableNamingStrategy getTableNamingStrategy() {
        return tableNamingStrategy;
    }

    public String getTableNameFormat() {
        return tableNameFormat;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public boolean isAutoEvolve() {
        return autoEvolve;
    }
}
