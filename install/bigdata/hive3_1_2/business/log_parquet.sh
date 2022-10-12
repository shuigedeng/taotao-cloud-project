#!/bin/bash

exec_date=$1
if [ "${exec_date}" ]; then
    exec_date=`date -d "${exec_date} 1 days ago" +%Y-%m-%d`
else
    exec_date=`date -d "1 days ago" +%Y-%m-%d`
fi

HIVE_HOST=localhost:10000
HIVE_USER=root

LOG_PARQUET_SQL="
  set hive.exec.dynamic.partition=true;
  set hive.exec.dynamic.partition.mode=nonstrict;

  insert overwrite table taotao_cloud_access_log_parquet
  partition(logday)
  select
    * ,
    logday
  from taotao_cloud_access_log_transform
  where logday=${exec_date};

  insert overwrite table taotao_cloud_sys_log_parquet
  partition(logday)
  select
    * ,
    logday
  from taotao_cloud_sys_log_sources
  where logday=${exec_date};

  insert overwrite table taotao_cloud_request_log_parquet
  partition(logday)
  select
    * ,
    logday
  from taotao_cloud_request_log_sources
  where logday=${exec_date};
"

echo "${LOG_PARQUET_SQL}"

hive -e "${LOG_PARQUET_SQL}"

# beeline -i ~/.hiverc -n ${HIVE_USER} -u jdbc:hive2://${HIVE_HOST} -e "${LOG_PARQUET_SQL}"
