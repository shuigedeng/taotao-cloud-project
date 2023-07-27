###########################################
cd /opt/soft

wget https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar

###########################################
cd /opt/soft

wget https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar

cp sentinel-dashboard-1.8.2.jar /opt/cloud/sentinel

 ##################### sentinel.sh #############################
 #!/bin/bash

 function start_sentinel() {
      nohup java -Dserver.port=8849 -Dcsp.sentinel.dashboard.server=192.168.10.220:8849  \
       -jar /opt/cloud/sentinel/sentinel-dashboard-1.8.2.jar  \
       >/opt/cloud/sentinel/start.log 2>&1 &
      sleep 10
      echo "sentinel started"
 }

 function stop_sentinel() {
      ps -ef | grep sentinel|grep -v grep|awk '{print $2}' |xargs kill -9
      sleep 10
      echo "sentinel stoped"
 }

 case $1 in
 "start")
     start_sentinel
     ;;
 "stop")
     stop_sentinel
     ;;
 "restart")
     stop_sentinel
     sleep 15
     start_sentinel
     ;;
 *)
     echo Invalid Args!
     echo 'Usage: '$(basename $0)' start|stop|restart'
     ;;
 esac



##################### sentinel.sh #############################
#!/bin/bash

function start_sentinel() {
     nohup java -Dserver.port=8849 -Dcsp.sentinel.dashboard.server=192.168.10.220:8849  \
      -jar /opt/cloud/sentinel/sentinel-dashboard-1.8.2.jar  \
      >/opt/cloud/sentinel/start.log 2>&1 &
     sleep 10
     echo "sentinel started"
}

function stop_sentinel() {
     ps -ef | grep sentinel|grep -v grep|awk '{print $2}' |xargs kill -9
     sleep 10
     echo "sentinel stoped"
}

case $1 in
"start")
    start_sentinel
    ;;
"stop")
    stop_sentinel
    ;;
"restart")
    stop_sentinel
    sleep 15
    start_sentinel
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac

