#!/bin/bash

current_dir=`dirname $(pwd)`

#JAVA_HOME="/opt/common/jdk-17"
#JAVA_HOME="/Users/dengtao/software/jdk-11.0.7/Contents/Home"
JAVA_HOME="/Users/dengtao/software/jdk-17.jdk/Contents/Home"

function install_dependencies() {
    cd $current_dir/taotao-cloud-dependencies
    gradle publishToMavenLocal -Dorg.gradle.java.home=$JAVA_HOME
}

function build_starters() {
    starter_dir=$current_dir/taotao-cloud-microservice/taotao-cloud-starter
    for starter in `ls $starter_dir`
    do
      if [ -d $starter_dir"/"$starter ];then
        cd $starter_dir"/"$starter
        gradle clean build clean build -x test -x bootJar -Dorg.gradle.java.home=$JAVA_HOME
      fi
    done
}

#install_dependencies

build_starters
