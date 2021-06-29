#!/bin/bash
### BEGIN INIT INFO
# Provides:          prometheus
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: starts prometheus
# Description:       starts the prometheus Process Manager daemon
### END INIT INFO
prefix=/opt/prometheus-2.23.0.linux-amd64
exec_prefix=${prefix}
prometheus_BIN=${exec_prefix}/prometheus
prometheus_LOG=${exec_prefix}/prometheus.log
prometheus_PID=${exec_prefix}/pid
 
case "$1" in
	start)
		if [[ -f $prometheus_PID ]]
			then
			if [[ ! -z `cat $prometheus_PID` ]]
				then
				echo -n "prometheus is running"
				exit 0
			fi
		fi
		echo -e "Starting prometheus \n"
		/usr/bin/nohup $prometheus_BIN --config.file="${exec_prefix}/prometheus.yml" --web.listen-address="0.0.0.0:9090" --storage.tsdb.path="/opt/prometheus-2.23.0.linux-amd64/data" --web.enable-lifecycle > $prometheus_LOG 2>&1 &
		echo $! > $prometheus_PID
	;;
	stop)
		if [[ ! -z `cat $prometheus_PID` ]]
			then
			echo -e "Stop prometheus \n"
			cat $prometheus_PID | xargs kill -9
		else
			echo -n "prometheus not running"
		fi
		echo > $prometheus_PID
	;;
	reload)
		if [[ -f $prometheus_PID ]]
			then
			kill -1 `cat $prometheus_PID`
		fi
	;;
	status)
		if [[ -z `cat $prometheus_PID` ]]
			then
			echo "prometheus is stopped"
		else
		    echo "prometheus is running"
		fi
	;;
	restart)
		$0 stop
		$0 start
	;;
	*)
		echo "Usage: $0 {start|stop|reload|status|restart}"
		exit 1
	;;
esac
