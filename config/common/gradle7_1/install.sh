#######################################33
https://gradle.org/releases/  选择Complete

https://gradle.org/next-steps/?version=7.1.1&format=all

unzip gradle-7.1.1-all.zip

mv gradle-7.1.1 /opt/common

vim $home/.bashrc

export GRADLE_HOM="/opt/common/gradle-7.1.1"
export GRADLE_USER_HOME="/opt/common/mvn_repo"
export PATH=$PATH:$GRADLE_HOM/bin
