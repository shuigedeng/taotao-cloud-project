#!/bin/bash
set -e

source ~/.bashrc

echo "listing data directories of $(hostname)"
ls /opt/hadoop

echo "Starting sshd service..."
/etc/init.d/ssh start

echo "Hostname: $(hostname)"
echo $HADOOP_HOME

initHiveSchema() {
  ($HIVE_HOME/bin/schematool -initSchema -ifNotExists -dbType mysql) || {
    return 0
  }
}

echo "NodeType: ${nodeType}"

if [ "${nodeType}" == "nameNone" ]; then
    #HDFS_ALREADY_FORMATTED=$(find "$HADOOP_HOME/data/" -mindepth 1 -print -quit 2>/dev/null)
    
    if [ -d "$HADOOP_HOME/data" ];then
        echo dir "$HADOOP_HOME/data" exist!
    else
        echo dir "$HADOOP_HOME/data" not exist!
        $HADOOP_HOME/bin/hdfs namenode -format || { echo 'FORMATTING FAILED' ; exit 1; }
    fi


    echo "Stopping HDFS..."
    $HADOOP_HOME/sbin/stop-all.sh

    # Checking if HDFS needs to be formated.
    #if [ !  $HDFS_ALREADY_FORMATTED ]; then
     #   echo "FORMATTING NAMENODE"
      #  $HADOOP_HOME/bin/hdfs namenode -format || { echo 'FORMATTING FAILED' ; exit 1; }
    #fi

    echo "Starting HDFS.."
    $HADOOP_HOME/sbin/start-dfs.sh

    echo "Initializing hive..."
    /bin/bash  /opt/apache-hive/bin/init-hive-dfs.sh

    echo "Initializing schemas hive..."
    initHiveSchema

    echo "Starting HIVESERVER.."
    $HIVE_HOME/bin/hive --service hiveserver2 &

    echo "Starting YARN."
    $HADOOP_HOME/sbin/start-yarn.sh

    echo "Starting webhttp hadoop service..."
    $HADOOP_HOME/bin/hdfs httpfs &

    echo "Create directories..."
    #Create non-existing folders
    $HADOOP_HOME/bin/hadoop fs -mkdir -p    /tmp
    $HADOOP_HOME/bin/hadoop fs -mkdir -p    /user/hive/warehouse
    $HADOOP_HOME/bin/hadoop fs -chmod 777   /tmp
    $HADOOP_HOME/bin/hadoop fs -chmod 777   /user/hive/warehouse

    # Create hue directories
    $HADOOP_HOME/bin/hadoop fs -mkdir -p /user/hue
    $HADOOP_HOME/bin/hadoop fs -chmod 777 /user/hue
fi

while true;
do
  sleep 30;
done;
