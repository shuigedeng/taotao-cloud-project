#!/bin/bash
set -e

source ~/.bashrc

echo "Starting sshd service..."
/etc/init.d/ssh start

if [ "${nodeType}" == 'master' ]; then
  echo "Start Spark master"
  /opt/spark/sbin/start-master.sh -h spark-master

  echo "Start jupyter"
  jupyter notebook --ip=0.0.0.0 --port=8899 --no-browser --allow-root --NotebookApp.allow_password_change=False --notebook-dir="./notebook"
else
  echo "Start Spark slave"
  /opt/spark/sbin/start-slave.sh spark://spark-master:7077
fi

while true;
do
  sleep 30;
done;
