#!/bin/bash

set -e

USER="backup"
PASSWORD="backup"
# 数据库数据目录 #
DATA_DIR="/data/mysql"
BIN_INDEX=$DATA_DIR"/mysql-bin.index"
# 备份目录 #
BACKUP_DIR="/data/backup/mysql"
BACKUP_LOG="/var/log/mysql/backup.log"

DATE=`date +"%Y%m%d"`
TIME=`date +"%Y%m%d%H"`

LOG_TIME=`date +"%Y-%m-%d %H:%M:%S"`
DELETE_BINLOG_TIME="7 day"
INCREMENT_INTERVAL="3 hour"

note() {
    printf "[$LOG_TIME] note: $*\n" >> $BACKUP_LOG;
}

warning() {
    printf "[$LOG_TIME] warning: $*\n" >> $BACKUP_LOG;
}

error() {
    printf "[$LOG_TIME] error: $*\n" >> $BACKUP_LOG;
    exit 1;
}

full_backup() {
    local dbs=`ls -l $DATA_DIR | grep "^d" | awk -F " " '{print $9}'`

    for db in $dbs
    do
        local backup_dir=$BACKUP_DIR"/full/"$db
        local filename=$db"."$DATE
        local backup_file=$backup_dir"/"$filename".sql"

        if [ ! -d $backup_dir ]
        then
            mkdir -p $backup_dir || { error "创建数据库 $db 全量备份目录 $backup_dir 失败"; continue; }
            note "数据库 $db 全量备份目录 $backup_dir  不存在，创建完成";
        fi

        note "full backup $db start ..."
        mysqldump --user=${USER} --password=${PASSWORD} --flush-logs --skip-lock-tables --quick $db > $backup_file || { warning "数据库 $db 备份失败"; continue; }

        cd $backup_dir
        tar -cPzf $filename".tar.gz" $filename".sql"
        rm -f $backup_file
        chown -fR mysql:mysql $backup_dir

        note "数据库 $db 备份成功";
        note "full backup $db end."
    done
}

increment_backup() {
    local StartTime=`date "-d $INCREMENT_INTERVAL ago" +"%Y-%m-%d %H:%M:%S"`
    local DELETE_BINLOG_END_TIME=`date "-d $DELETE_BINLOG_TIME ago" +"%Y-%m-%d %H:%M:%S"`
    local dbs=`ls -l $DATA_DIR | grep "^d" | awk -F " " '{print $9}'`

    mysql -u$USER -p$PASSWORD -e "purge master logs before '$DELETE_BINLOG_END_TIME'" && note "delete $DELETE_BINLOG_TIME days before log";

    filename=`cat $BIN_INDEX | awk -F "/" '{print $2}'`
    for i in $filename
    do
        for db in $dbs
        do
            local backup_dir=$BACKUP_DIR"/increment/"$db
            local filename=$db"."$TIME
            local backup_file=$backup_dir"/"$filename".sql"

            if [ ! -d $backup_dir ]
            then
                mkdir -p $backup_dir || { error "创建数据库 $db 增量备份目录 $backup_dir 失败"; continue; }
                note "数据库 $db 增量备份目录 $backup_dir  不存在，创建完成";
            fi

            note "increment backup $db form time $StartTime start ..."

            mysqlbinlog -d $db --start-datetime="$StartTime" $DATA_DIR/$i >> $backup_file || { warning "数据库 $db 备份失败"; continue; }

            note "increment backup $db end."
        done
    done

    for db in $dbs
    do
        local backup_dir=$BACKUP_DIR"/increment/"$db
        local filename=$db"."$TIME
        local backup_file=$backup_dir"/"$filename".sql"

        cd $backup_dir
        tar -cPzf $filename".tar.gz" $filename".sql"
        rm -f $backup_file

        note "数据库 $db 备份成功";
    done
}

case "$1" in
    full)
        full_backup
    ;;
    increment)
        increment_backup
    ;;
    *)
        exit 2
    ;;
esac

exit 1
