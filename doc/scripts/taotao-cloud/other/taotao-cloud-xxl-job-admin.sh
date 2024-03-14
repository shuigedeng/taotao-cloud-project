#!/bin/sh

# 该脚本为Linux下启动java程序的脚本
#
# author: shuigedeng
# date: 2021/1/15
#
# 特别注意：
# 该脚本使用系统kill命令来强制终止指定的java程序进程。
# 所以在杀死进程前，可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本，

# 根据实际情况来修改以下配置信息 ##################################

VERSION=2021.8
APP_NAME=taotao-cloud-xxl-job-admin

# jar包存放路径
JAR_PATH="/opt/taotao-cloud/${APP_NAME}"
# jar包名称
JAR_NAME=${APP_NAME}-${VERSION}.jar
# PID 代表是PID文件
JAR_PID=$JAR_PATH/$APP_NAME\.pid
# 日志输出文件
LOG_FILE=$JAR_PATH

# java虚拟机启动参数
JAVA_OPTS="
-Xms512m
-Xmx512m
-XX:MetaspaceSize=512m
-XX:MaxMetaspaceSize=1024m
-XX:ParallelGCThreads=4
-XX:+PrintGCDateStamps
-XX:+PrintGCDetails
-XX:+HeapDumpOnOutOfMemoryError
-Dfile.encoding=utf-8
-javaagent:/opt/cloud/apache-skywalking-apm-bin/agent/skywalking-agent.jar
-Dskywalking.agent.service_name=${APP_NAME}
-Dskywalking.agent.authentication=taotao-cloud
-Dskywalking.logging.file_name=skywalking.log
-Dskywalking.logging.level=INFO
-Dskywalking.logging.dir=${JAR_PATH}
-Dskywalking.collector.backend_service=192.168.10.200:11800
"

# 根据实际情况来修改以上配置信息 ##################################

# 检查程序是否处于运行状态
is_exist() {
  # 查询出应用服务的进程id，grep -v 是反向查询的意思，查找除了grep操作的run.jar的进程之外的所有进程
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `

  # [ ]表示条件测试。注意这里的空格很重要。要注意在'['后面和']'前面都必须要有空格
  # [ -z STRING ] 如果STRING的长度为零则返回为真，即空是真
  # 如果不存在返回0，存在返回1
  if [ -z "${pid}" ]; then
   return 0
  else
    return 1
  fi
}

# ######### Shell脚本中$0、$?、$!、$$、$*、$#、$@等的说明 #########

# $$ Shell本身的PID（ProcessID，即脚本运行的当前 进程ID号）
# $! Shell最后运行的后台Process的PID(后台运行的最后一个进程的 进程ID号)
# $? 最后运行的命令的结束代码（返回值）即执行上一个指令的返回值 (显示最后命令的退出状态。0表示没有错误，其他任何值表明有错误)
# $- 显示shell使用的当前选项，与set命令功能相同
# $* 所有参数列表。如"$*"用「"」括起来的情况、以"$1 $2 … $n"的形式输出所有参数，此选项参数可超过9个。
# $@ 所有参数列表。如"$@"用「"」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。
# $# 添加到Shell的参数个数
# $0 Shell本身的文件名
# $1～$n 添加到Shell的各参数值。$1是第1参数、$2是第2参数…。


# 服务启动方法
start() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$APP_NAME is already running pid is ${pid}"
  else
    # jar服务启动脚本
    nohup java $JAVA_OPTS \
    -Xloggc:$LOG_FILE/gclog.log \
    -XX:HeapDumpPath=$LOG_FILE/HeapDump.hprof \
    -jar $JAR_PATH/$JAR_NAME  >$LOG_FILE/$APP_NAME\.log 2>&1 &

    echo $! > $JAR_PID
    echo "start $APP_NAME successed pid is $! "
   fi
}

  # 服务停止方法
stop() {
  # is_exist
  pidf=$(cat $JAR_PID)
  # echo "$pidf"
  echo "pid = $pidf begin kill $pidf"
  kill $pidf
  rm -rf $JAR_PID
  sleep 2
  # 判断服务进程是否存在
  is_exist
  if [ $? -eq "1" ]; then
    echo "pid = $pid begin kill -9 $pid"
    kill -9  $pid
    sleep 2
    echo "$APP_NAME process stopped！"
  else
    echo "$APP_NAME is not running！"
  fi
}

# 服务运行状态查看方法
status() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$APP_NAME is running，pid is ${pid}"
  else
    echo "$APP_NAME is not running！"
  fi
}

# 重启服务方法
restart() {
  # 调用服务停止命令
  stop
  # 调用服务启动命令
  start
}

# 帮助说明，用于提示输入参数信息
usage() {
    echo "Usage: sh run-service.sh [ start | stop | restart | status ]"
    exit 1
}

###################################
# 读取脚本的第一个参数($1)，进行判断
# 参数取值范围：{ start | stop | restart | status }
# 如参数不在指定范围之内，则打印帮助信息
###################################
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  'start')
    start
    ;;
  'stop')
    stop
    ;;
  'restart')
    restart
    ;;
  'status')
    status
    ;;
  *)
    usage
    ;;
esac
exit 0

