#######################################33
https://gradle.org/releases/  选择Complete

https://gradle.org/next-steps/?version=7.1.1&format=all

unzip gradle-7.1.1-all.zip

mv gradle-7.1.1 /opt/taotao-common

vim $home/.bashrc

export GRADLE_HOM="/opt/taotao-common/gradle7.1"
export GRADLE_USER_HOME="/opt/taotao-common/mvn_repo"
export PATH=$PATH:$GRADLE_HOM/bin
