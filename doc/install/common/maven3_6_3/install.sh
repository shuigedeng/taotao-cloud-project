##############################
http://maven.apache.org/download.cgi

wget https://mirrors.bfsu.edu.cn/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.zip

unzip apache-maven-3.8.1-bin.zip

mv apache-maven-3.8.1 /opt/common

export MAVEN_HOME="/opt/common/apache-maven-3.8.1"
export M2_HOME="/opt/common/apache-maven-3.8.1"
export PATH=${PATH}:$MAVEN_HOME/bin

vim conf/settings.xml

<localRepository>/opt/common/mvn_repo</localRepository>

<mirror>
  <id>alimaven</id>
  <name>aliyun maven</name>
  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
  <mirrorOf>central</mirrorOf>
</mirror>
