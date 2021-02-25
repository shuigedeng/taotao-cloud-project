# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# User specific aliases and functions

export HADOOP_HOME=/opt/hadoop

#Hadoop variables
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64


# SET HADOOP
export HADOOP_HOME="/opt/hadoop"
export HADOOP_USER_NAME="root"
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_LIBEXEC_DIR=${HADOOP_HOME}/libexec

# SET YEARN
export YARN_HOME=$HADOOP_HOME
export YARN_RESOURCEMANAGER_USER="root"
export YARN_NODEMANAGER_USER="root"

# SET HDFS
export HDFS_NAMENODE_USER="root"
export HDFS_DATANODE_USER="root"
export HDFS_SECONDARYNAMENODE_USER="root"

# SET HIVE
export HIVE_HOME=/opt/apache-hive
export HIVE_PORT=10000
export HIVE_CONF_DIR=$HIVE_HOME/conf

# LIBS
export CLASSPATH=$CLASSPATH:$HADOOP_HOME/lib/*:.
export CLASSPATH=$CLASSPATH:$HIVE_HOME/lib/*:.

# PATH
export PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${JAVA_HOME}:${HADOOP_MAPRED_HOME}:${HADOOP_COMMON_HOME}:${HADOOP_HDFS_HOME}:${YARN_HOME}:${HADOOP_LIBEXEC_DIR}:${HIVE_HOME}/bin
