##############################
http://maven.apache.org/download.cgi

wget https://mirrors.bfsu.edu.cn/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.zip

unzip apache-maven-3.8.1-bin.zip

mv apache-maven-3.8.1-bin /opt/taotao-common

export MAVEN_HOME="/opt/taotao-common/apache-maven-3.8.1"
export M2_HOME="/opt/taotao-common/apache-maven-3.8.1"
export PATH=${PATH}:$MAVEN_HOME/bin

vim /opt/taotao-common/apache-maven-3.8.1/conf/settins.xml

<localRepository>/opt/taotao-common/mvn_repo</localRepository>

<mirrors>
   <mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
  </mirror>
</mirrors>
