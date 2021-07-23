export JAVA_HOME="/opt/common/jdk1.8.0_211"
export PATH=$PATH:$JAVA_HOME/bin
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH

export SCALA_HOME="/opt/taotao-common/scala2.12.11"
export PATH=$PATH:$SCALA_HOME/bin

export MAVEN_HOME="/opt/taotao-common/maven3.6.3"
export PATH=$PATH:$MAVEN_HOME/bin

export GRADLE_HOM="/opt/taotao-common/gradle7.1"
export PATH=$PATH:$GRADLE_HOM/bin


export HADOOP_HOME="/opt/bigdata/hadoop-3.3.0"
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_YARN_HOME=$HADOOP_HOME

export HADOOP_INSTALL=$HADOOP_HOME
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_LIBEXEC_DIR=$HADOOP_HOME/libexec
export JAVA_LIBRARY_PATH=$HADOOP_HOME/lib/native:$JAVA_LIBRARY_PATH
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop

export HADOOP_USER="root"
export HDFS_DATANODE_USER=root
#export HDFS_DATANODE_SECURE_USER=root
export HDFS_NAMENODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root

export YARN_HOME=$HADOOP_HOME
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root

export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

#export TEZ_CONF_DIR=$HADOOP_CONF_DIR
#export TEZ_JARS=/taotao-cloud/hive-3.1.2/*:/taotao-cloud/hive-3.1.2/lib/*
#export HADOOP_CLASSPATH=$TEZ_CONF_DIR:$TEZ_JARS:$HADOOP_CLASSPATH

export HIVE_HOME="/opt/taotao-bigdata/hive3.1.2"
export PATH=$PATH:$HIVE_HOME/bin

export SPARK_HOME="/opt/taotao-bigdata/spark3.0.0"
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

export FLINK_HOME="/opt/taotao-bigdata/flink1.9.3"
export PATH=$FLINK_HOME/bin:$PATH

export FLUME_HOME="/opt/taotao-bigdata/flume1.9.0"
export PATH=$PATH:$FLUME_HOME/bin

export ZK_HOME="/opt/taotao-cloud/zookeeper-3.6.2"
export PATH=$PATH:$ZK_HOME/bin

export LD_LIBRARY_PATH="/opt/taotao-common/protobuf-2.5.0"
export PATH=$PATH:$LD_LIBRARY_PATH

export SQOOP_HOME="/opt/taotao-bigdata/sqoop1.4.7"
export PATH=$PATH:$SQOOP_HOME/bin

# User specific aliases and functions

alias rm='rm -i'
alias cp='cp -i'
alias mv='mv -i'

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi
