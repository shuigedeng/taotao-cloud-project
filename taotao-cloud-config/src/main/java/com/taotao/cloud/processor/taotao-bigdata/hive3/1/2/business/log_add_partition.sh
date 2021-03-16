#!/bin/bash

exec_date=$1
if [ "${exec_date}" ]; then
    exec_date=`date -d "${exec_date} 1 days ago" +%Y-%m-%d`
else
    exec_date=`date -d "1 days ago" +%Y-%m-%d`
fi

SQL_DATE=${exec_date}

HIVE_HOST=localhost:10000
HIVE_USER=root

ADD_PARTITION_SQL="
  ALTER TABLE `taotao_cloud_access_log_transform` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_access_log_transform` add partition (logday = "${exec_date}") location "/taotao/cloud/access/log/transform/${exec_date}"

  ALTER TABLE `taotao_cloud_access_log_parquet` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_access_log_parquet` add partition (logday = "${exec_date}") location "/taotao/cloud/access/log/parquet/${exec_date}"

  ALTER TABLE `taotao_cloud_sys_log_source` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_sys_log_source` add partition (logday = "${exec_date}") location "/taotao/cloud/sys/log/sources/${exec_date}"

  ALTER TABLE `taotao_cloud_sys_log_parquet` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_sys_log_parquet` add partition (logday = "${exec_date}") location "/taotao/cloud/sys/log/parquet/${exec_date}"

  ALTER TABLE `taotao_cloud_request_log_source` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_request_log_source` add partition (logday = "${exec_date}") location "/taotao/cloud/request/log/sources/${exec_date}"

  ALTER TABLE `taotao_cloud_request_log_parquet` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_request_log_parquet` add partition (logday = "${exec_date}") location "/taotao/cloud/request/log/parquet/${exec_date}"

  ALTER TABLE `taotao_cloud_biz_order_log_parquet` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_biz_order_log_parquet` add partition (logday = "${exec_date}") location "/taotao/cloud/biz/member/login/log/parquet/${exec_date}"

  ALTER TABLE `taotao_cloud_biz_member_login_log_parquet` DROP IF EXISTS PARTITION (logday = "${exec_date}");
  alter table `taotao_cloud_biz_member_login_log_parquet` add partition (logday = "${exec_date}") location "/taotao/cloud/biz/member/login/log/parquet/${exec_date}"
"

hive -e "${ADD_PARTITION_SQL}"

# beeline -i ~/.hiverc -n ${HIVE_USER} -u jdbc:hive2://${HIVE_HOST} -e "${ADD_PARTITION_SQL}"
