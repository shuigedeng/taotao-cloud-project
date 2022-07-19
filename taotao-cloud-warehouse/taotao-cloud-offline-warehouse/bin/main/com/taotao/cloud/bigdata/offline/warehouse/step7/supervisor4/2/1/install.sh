#/bin/bash

dnf install epel-release

dnf install supervisor

# 备份配置文件
cp /etc/supervisord.conf /etc/supervisord.conf.bak

vim supervisord.conf
# 打开 serverurl=http://127.0.0.1:9001
# files = supervisord.d/*.conf

supervisorctl reload

##################### supervisord.sh #############################
#!/bin/bash

function start_supervisord() {
     systemctl start supervisord
     sleep 10
     echo "supervisord started"
}

function stop_supervisord() {
     systemctl stop supervisord
     sleep 10
     echo "supervisord stoped"
}

case $1 in
"start")
    start_supervisord
    ;;
"stop")
    stop_supervisord
    ;;
"restart")
    stop_supervisord
    sleep 10
    start_supervisord
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac


