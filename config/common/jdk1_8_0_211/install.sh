###################下载地址#########################
https://repo.huaweicloud.com/java/jdk
https://mirrors.tuna.tsinghua.edu.cn/AdoptOpenJDK/
http://www.codebaoku.com/jdk/jdk-openjdk.html
http://www.codebaoku.com/jdk/jdk-oracle.html
http://www.codebaoku.com/jdk/jdk-adoptopenjdk.html

tar -zxvf

export JAVA_HOME="/opt/taotao-common/jdk1.8.0_211"
export PATH=$PATH:$JAVA_HOME/bin
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
