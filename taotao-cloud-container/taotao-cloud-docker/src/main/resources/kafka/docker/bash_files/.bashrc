# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# User specific aliases and functions

# SET JAVA
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# SET HADOOP
export KAFKA_HOME=/opt/kafka

# PATH
export PATH=${PATH}:${JAVA_HOME}:${KAFKA_HOME}/bin
