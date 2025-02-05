SET 'execution.checkpointing.interval' = '100s';
SET 'table.exec.state.ttl'= '8640000';
SET 'table.exec.mini-batch.enabled' = 'true';
SET 'table.exec.mini-batch.allow-latency' = '60s';
SET 'table.exec.mini-batch.size' = '10000';
SET 'table.local-time-zone' = 'Asia/Shanghai';
SET 'table.exec.sink.not-null-enforcer'='DROP';
SET 'table.exec.sink.upsert-materialize' = 'NONE';
SET 'execution.runtime-mode' = 'streaming';


CREATE CATALOG iceberg_catalog WITH (
    'type' = 'iceberg',
    'metastore' = 'hive',
    'uri' = 'thrift://192.168.244.129:9083',
    'hive-conf-dir' = '/opt/software/apache-hive-3.1.3-bin/conf',
    'hadoop-conf-dir' = '/opt/software/hadoop-3.1.3/etc/hadoop',
    'warehouse' = 'hdfs:////user/hive/warehouse'
);

use CATALOG iceberg_catalog;

create  DATABASE IF NOT EXISTS iceberg_dws;

CREATE TABLE IF NOT EXISTS iceberg_dws.dws_trade_user_sku_order_refund_1d_full(
    `user_id`                    BIGINT COMMENT '用户id',
    `sku_id`                     BIGINT COMMENT 'sku_id',
    `k1`                         STRING COMMENT '分区字段',
    `sku_name`                   STRING COMMENT 'sku名称',
    `category1_id`               BIGINT COMMENT '一级分类id',
    `category1_name`             STRING COMMENT '一级分类名称',
    `category2_id`               BIGINT COMMENT '一级分类id',
    `category2_name`             STRING COMMENT '一级分类名称',
    `category3_id`               BIGINT COMMENT '一级分类id',
    `category3_name`             STRING COMMENT '一级分类名称',
    `tm_id`                      BIGINT COMMENT '品牌id',
    `tm_name`                    STRING COMMENT '品牌名称',
    `order_refund_count_1d`      BIGINT COMMENT '最近1日退单次数',
    `order_refund_num_1d`        BIGINT COMMENT '最近1日退单件数',
    `order_refund_amount_1d`     DECIMAL(16, 2) COMMENT '最近1日退单金额',
    PRIMARY KEY (`user_id`,`k1` ) NOT ENFORCED
    )   PARTITIONED BY (`k1` ) WITH (
    'catalog-name'='hive_prod',
    'uri'='thrift://192.168.244.129:9083',
    'warehouse'='hdfs://192.168.244.129:9000/user/hive/warehouse/'
    );


INSERT INTO iceberg_dws.dws_trade_user_sku_order_refund_1d_full /*+ OPTIONS('upsert-enabled'='true') */ (
    user_id,
    sku_id,
    k1,
    sku_name,
    category1_id,
    category1_name,
    category2_id,
    category2_name,
    category3_id,
    category3_name,
    tm_id,
    tm_name,
    order_refund_count_1d,
    order_refund_num_1d,
    order_refund_amount_1d
    )
select
    user_id,
    sku_id,
    k1,
    sku_name,
    category1_id,
    category1_name,
    category2_id,
    category2_name,
    category3_id,
    category3_name,
    tm_id,
    tm_name,
    order_refund_count,
    order_refund_num,
    order_refund_amount
from
    (
        select
            user_id,
            sku_id,
            k1,
            count(*) order_refund_count,
            sum(refund_num) order_refund_num,
            sum(refund_amount) order_refund_amount
        from iceberg_dwd.dwd_trade_order_refund_full /*+ OPTIONS('streaming'='true', 'monitor-interval'='1s')*/
        group by user_id,sku_id,k1
    )od
        left join
    (
        select
            id,
            sku_name,
            category1_id,
            category1_name,
            category2_id,
            category2_name,
            category3_id,
            category3_name,
            tm_id,
            tm_name
        from iceberg_dim.dim_sku_full /*+ OPTIONS('streaming'='true', 'monitor-interval'='1s')*/
    )sku
    on od.sku_id=sku.id;