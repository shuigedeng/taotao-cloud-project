#/bin/bash

# filename: spilt-access-log.sh
# date: 2020-11-23

usage(){
  echo "usage"
  echo "spilt-access-log.sh [-f log_file] [-d data_dir] [-p pid_file]"
  echo "description"
  echo "log_file: nginx access file absolute path"
  echo "data_dir: spilt data dir"
  echo "pid_file: nginx pid file absolute path"
  echo "Waning: if no params use default"
  exit 1
}

default(){
  echo "use default"
  echo "log_file=/opt/openresty/weblog/taotao-cloud-log.access.log"
  echo "data_dir=/opt/openresty/weblog/data/"
  echo "pid_file=/opt/openresty/pid/nginx.pid"

  log_file="/opt/openresty/weblog/taotao-cloud-log.access.log"
  data_dir="/opt/openresty/weblog/data/"
  pid_file="/opt/openresty/pid/nginx.pid"
}

while getopts 'f:d:p:h' OPT; do
    case $OPT in
    f) log_file="$OPTARG";;
    d) log_file="$OPTARG";;
    p) log_file="$OPTARG";;

    h) usage;;
    ?) usage;;
    *) usage;;
    esac
done

if [ $# -eq 0 ]; then
    default
fi

if [ ! "${log_file}" ] || [ ! "${data_dir}" ] || [ ! ${pid_file} ]; then
    echo "params is empty"
    exit -1
fi

line=`tail -n 1 ${log_file}`
if [ ! "$line" ]; then
    echo "access log no data no spilt"
    exit 0
fi

mv ${log_file} ${data_dir}taotao-cloud.access.$(date +"%s").log

kill -USR1 `cat ${pid_file}`

echo "finish"
