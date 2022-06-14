# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# User specific aliases and functions

# SET JAVA
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# SET HADOOP
export FLUME_HOME=/opt/flume

# PATH
export PATH=${PATH}:${JAVA_HOME}:${FLUME_HOME}/bin
