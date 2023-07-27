###########################################
cd /root/taotao-cloud/mysql8

wget https://github.com/prometheus/mysqld_exporter/releases/download/v0.13.0/mysqld_exporter-0.13.0.linux-amd64.tar.gz

tar -zxvf mysqld_exporter-0.13.0.linux-amd64.tar.gz

mv mysqld_exporter-0.13.0.linux-amd64 mysqld_exporter

vim /mysqld_exporter/my.cnf
[client]
host=192.168.10.220
user=root
port=3306
password=123456

nohup /root/taotao-cloud/mysql8/mysqld_exporter/mysqld_exporter  \
--config.my-cnf=/root/taotao-cloud/mysql8/mysqld_exporter/.my.cnf \
--web.listen-address=9104 \
>/root/taotao-cloud/mysql8/mysqld_exporter/mysqld_exporter_9104.out 2>&1 &

netstat -lnptu | grep 9104

- job_name: 'mysql-node2'
  static_configs:
    - targets: ['192.168.10.220:9104']
      labels:
        instance: db-02

- job_name: 'mysql-node1'
  static_configs:
    - targets: ['192.168.1.11:9103']
      labels:
        instance: db-01

curl  -X POST localhost:9090/-/reload

# grafana导入模板模板编号7362

################################ 告警规则 ######################################

groups:
- name: MySQLStatsAlert
  rules:
  - alert: MySQL is down
    expr: mysql_up == 0
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} MySQL is down"
      description: "MySQL database is down. This requires immediate action!"
  - alert: open files high
    expr: mysql_global_status_innodb_num_open_files > (mysql_global_variables_open_files_limit) * 0.75
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} open files high"
      description: "Open files is high. Please consider increasing open_files_limit."
  - alert: Read buffer size is bigger than max. allowed packet size
    expr: mysql_global_variables_read_buffer_size > mysql_global_variables_slave_max_allowed_packet
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Read buffer size is bigger than max. allowed packet size"
      description: "Read buffer size (read_buffer_size) is bigger than max. allowed packet size (max_allowed_packet).This can break your replication."
  - alert: Sort buffer possibly missconfigured
    expr: mysql_global_variables_innodb_sort_buffer_size <256*1024 or mysql_global_variables_read_buffer_size > 4*1024*1024
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Sort buffer possibly missconfigured"
      description: "Sort buffer size is either too big or too small. A good value for sort_buffer_size is between 256k and 4M."
  - alert: Thread stack size is too small
    expr: mysql_global_variables_thread_stack <196608
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Thread stack size is too small"
      description: "Thread stack size is too small. This can cause problems when you use Stored Language constructs for example. A typical is 256k for thread_stack_size."
  - alert: Used more than 80% of max connections limited
    expr: mysql_global_status_max_used_connections > mysql_global_variables_max_connections * 0.8
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Used more than 80% of max connections limited"
      description: "Used more than 80% of max connections limited"
  - alert: InnoDB Force Recovery is enabled
    expr: mysql_global_variables_innodb_force_recovery != 0
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} InnoDB Force Recovery is enabled"
      description: "InnoDB Force Recovery is enabled. This mode should be used for data recovery purposes only. It prohibits writing to the data."
  - alert: InnoDB Log File size is too small
    expr: mysql_global_variables_innodb_log_file_size < 16777216
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} InnoDB Log File size is too small"
      description: "The InnoDB Log File size is possibly too small. Choosing a small InnoDB Log File size can have significant performance impacts."
  - alert: InnoDB Flush Log at Transaction Commit
    expr: mysql_global_variables_innodb_flush_log_at_trx_commit != 1
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} InnoDB Flush Log at Transaction Commit"
      description: "InnoDB Flush Log at Transaction Commit is set to a values != 1. This can lead to a loss of commited transactions in case of a power failure."
  - alert: Table definition cache too small
    expr: mysql_global_status_open_table_definitions > mysql_global_variables_table_definition_cache
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Table definition cache too small"
      description: "Your Table Definition Cache is possibly too small. If it is much too small this can have significant performance impacts!"
  - alert: Table open cache too small
    expr: mysql_global_status_open_tables >mysql_global_variables_table_open_cache * 99/100
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Table open cache too small"
      description: "Your Table Open Cache is possibly too small (old name Table Cache). If it is much too small this can have significant performance impacts!"
  - alert: Thread stack size is possibly too small
    expr: mysql_global_variables_thread_stack < 262144
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Thread stack size is possibly too small"
      description: "Thread stack size is possibly too small. This can cause problems when you use Stored Language constructs for example. A typical is 256k for thread_stack_size."
  - alert: InnoDB Buffer Pool Instances is too small
    expr: mysql_global_variables_innodb_buffer_pool_instances == 1
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} InnoDB Buffer Pool Instances is too small"
      description: "If you are using MySQL 5.5 and higher you should use several InnoDB Buffer Pool Instances for performance reasons. Some rules are: InnoDB Buffer Pool Instance should be at least 1 Gbyte in size. InnoDB Buffer Pool Instances you can set equal to the number of cores of your machine."
  - alert: InnoDB Plugin is enabled
    expr: mysql_global_variables_ignore_builtin_innodb == 1
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} InnoDB Plugin is enabled"
      description: "InnoDB Plugin is enabled"
  - alert: Binary Log is disabled
    expr: mysql_global_variables_log_bin != 1
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Binary Log is disabled"
      description: "Binary Log is disabled. This prohibits you to do Point in Time Recovery (PiTR)."
  - alert: Binlog Cache size too small
    expr: mysql_global_variables_binlog_cache_size < 1048576
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Binlog Cache size too small"
      description: "Binlog Cache size is possibly to small. A value of 1 Mbyte or higher is OK."
  - alert: Binlog Statement Cache size too small
    expr: mysql_global_variables_binlog_stmt_cache_size <1048576 and mysql_global_variables_binlog_stmt_cache_size > 0
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Binlog Statement Cache size too small"
      description: "Binlog Statement Cache size is possibly to small. A value of 1 Mbyte or higher is typically OK."
  - alert: Binlog Transaction Cache size too small
    expr: mysql_global_variables_binlog_cache_size  <1048576
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Binlog Transaction Cache size too small"
      description: "Binlog Transaction Cache size is possibly to small. A value of 1 Mbyte or higher is typically OK."
  - alert: Sync Binlog is enabled
    expr: mysql_global_variables_sync_binlog == 1
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Sync Binlog is enabled"
      description: "Sync Binlog is enabled. This leads to higher data security but on the cost of write performance."
  - alert: IO thread stopped
    expr: mysql_slave_status_slave_io_running != 1
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} IO thread stopped"
      description: "IO thread has stopped. This is usually because it cannot connect to the Master any more."
  - alert: SQL thread stopped
    expr: mysql_slave_status_slave_sql_running == 0
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} SQL thread stopped"
      description: "SQL thread has stopped. This is usually because it cannot apply a SQL statement received from the master."
  - alert: SQL thread stopped
    expr: mysql_slave_status_slave_sql_running != 1
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Instance {{ $labels.instance }} Sync Binlog is enabled"
      description: "SQL thread has stopped. This is usually because it cannot apply a SQL statement received from the master."
  - alert: Slave lagging behind Master
    expr: rate(mysql_slave_status_seconds_behind_master[1m]) >30
    for: 1m
    labels:
      severity: warning
    annotations:
      summary: "Instance {{ $labels.instance }} Slave lagging behind Master"
      description: "Slave is lagging behind Master. Please check if Slave threads are running and if there are some performance issues!"
  - alert: Slave is NOT read only(Please ignore this warning indicator.)
    expr: mysql_global_variables_read_only != 0
    for: 1m
    labels:
      severity: page
    annotations:
      summary: "Instance {{ $labels.instance }} Slave is NOT read only"
      description: "Slave is NOT set to read only. You can accidentally manipulate data on the slave and get inconsistencies..."
