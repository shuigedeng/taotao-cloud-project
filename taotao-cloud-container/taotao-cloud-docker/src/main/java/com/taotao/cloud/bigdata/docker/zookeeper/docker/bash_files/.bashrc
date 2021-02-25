# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# User specific aliases and functions

# SET JAVA
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

# SET ZOOKEEPER
export ZOOKEEPER_HOME=/usr/share/zookeeper

# PATH
export PATH=${PATH}:${JAVA_HOME}:${ZOOKEEPER_HOME}/bin
