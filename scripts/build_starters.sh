#!/bin/bash

JAVA_HOME="/opt/common/jdk-17"
#JAVA_HOME="/Users/dengtao/software/jdk-11.0.7/Contents/Home"

function build_starters() {
    for file in `ls $1`
    do
      if [ -d $1"/"$file ];then
        cd $1"/"$file
        gradle clean build -Dorg.gradle.java.home=$JAVA_HOME
      fi
    done

    cd .
}

build_starters $(dirname $(pwd))/taotao-cloud-microservice/taotao-cloud-starter
