#!/bin/bash

MYSQL_CONN=jdbc:mysql://127.0.0.1:3306/taotao-cloud-log-service
MYSQL_USERNAME=root
MYSQL_PASSWORD=123456

exec_date=$1
if [ "${exec_date}" ]; then
    exec_date=`date -d "${exec_date} 1 days ago" +%Y-%m-%d`
else
    exec_date=`date -d "1 days ago" +%Y-%m-%d`
fi

SQL_DATE=${exec_date}

-- meta 表
$SQOOP_HOME/bin/sqoop import \
--connect ${MYSQL_CONN} \
--username ${MYSQL_USERNAME} \
--password ${MYSQL_PASSWORD} \
-e "
  select id,
       meta_type,
       field,
       field_type,
       field_desc,
       meta_version,
       status,
       date_format(create_time, '%Y-%m-%d %H:%i:%s') as create_time
from log_meta
where 1 = 1 and create_time <= '${SQL_DATE} 00:00:00'; " \
--split-by id \
--as-parquetfile \
--target-dir /taotao/cloud/meta/${exec_date} \
--delete-target-dir
-m 3

-- 会员登录日志表
$SQOOP_HOME/bin/sqoop import \
--connect ${MYSQL_CONN} \
--username ${MYSQL_USERNAME} \
--password ${MYSQL_PASSWORD} \
-e "
       select id,
       member_id,
       login_time,
       login_ip,
       login_status
from tt_member_login
where 1 = 1 and create_time <= '${SQL_DATE} 00:00:00'; " \
--split-by id \
--as-parquetfile \
--target-dir /taotao/cloud/biz/member/login/log/parquet/${exec_date} \
--delete-target-dir
-m 3

-- 订单日志表
$SQOOP_HOME/bin/sqoop import \
--connect ${MYSQL_CONN} \
--username ${MYSQL_USERNAME} \
--password ${MYSQL_PASSWORD} \
-e "
  select id,
       order_id,
       member_id,
       type,
       msg,
       data,
       ctime
from tt_order_log
where 1 = 1 and create_time <= '${SQL_DATE} 00:00:00'; " \
--split-by id \
--as-parquetfile \
--target-dir /taotao/cloud/biz/order/log/parquet/${exec_date} \
--delete-target-dir
-m 3
